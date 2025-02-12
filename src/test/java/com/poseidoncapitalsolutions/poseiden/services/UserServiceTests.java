package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.UserDTO;
import com.poseidoncapitalsolutions.poseiden.domain.User;
import com.poseidoncapitalsolutions.poseiden.exceptions.UserAlreadyExistsException;
import com.poseidoncapitalsolutions.poseiden.exceptions.UserIdNotFoundException;
import com.poseidoncapitalsolutions.poseiden.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	private User dummyUser;

	@BeforeEach
	void setUp() {
		dummyUser = new User();
		dummyUser.setUsername("j.doe");
		dummyUser.setFullname("John Doe");
		dummyUser.setPassword("val1dP@ssword");
		dummyUser.setRole("USER");
	}

	@Nested
	@DisplayName("getAll() Tests")
	class GetAllTests {
		@Test
		@DisplayName("Should return all users")
		public void getAllTest() {
			User dummyUser2 = new User();
			dummyUser2.setUsername("j.smith");
			dummyUser2.setFullname("Jane Smith");
			dummyUser2.setPassword("val1dP@ssword");
			dummyUser2.setRole("USER");

			when(userRepository.findAll()).thenReturn(List.of(dummyUser, dummyUser2));

			List<User> users = userService.getAll();

			assertThat(users).hasSize(2);
		}
	}

	@Nested
	@DisplayName("getById() Tests")
	class GetByIdTests {
		@Test
		@DisplayName("Should return a user with the given ID")
		public void givenUserNotExists_whenGetById_thenReturnUser() {
			when(userRepository.findById(anyInt())).thenReturn(Optional.of(dummyUser));

			User user = userService.getById(1);

			assertThat(user).isNotNull();
			assertThat(user).isEqualTo(dummyUser);

			verify(userRepository, times(1)).findById(eq(1));
			verifyNoMoreInteractions(userRepository);
		}

		@Test
		@DisplayName("Should throw an exception if user with the given ID does NOT exist")
		public void givenUserNotExists_whenGetById_thenThrowException() {
			Integer userId = 1;
			when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

			RuntimeException exception = assertThrows(UserIdNotFoundException.class, () -> {
				userService.getById(userId);
			});

			String expectedExceptionMessage = String.format("User with Id '%s' doesn't exist.", userId);

			assertThat(exception.getMessage()).isEqualTo(expectedExceptionMessage);

			verify(userRepository, times(1)).findById(userId);
			verifyNoMoreInteractions(userRepository);
		}
	}

	@Nested
	@DisplayName("save() Tests")
	class SaveTests {
		private UserDTO dummyUserDTO;

		@BeforeEach
		void setUp() {
			dummyUserDTO = new UserDTO();
			dummyUserDTO.setUsername("j.doe");
			dummyUserDTO.setFullname("John Doe");
			dummyUserDTO.setPassword("val1dP@ssword");
			dummyUserDTO.setRole("USER");

			dummyUser.setPassword("encodedPassword");
		}

		@Test
		@DisplayName("Should save a user if NOT already exists")
		public void givenUserNotExists_whenSaveUser_thenSaveUser() {
			when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
			when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
			when(userRepository.save(any(User.class))).thenReturn(dummyUser);

			User user = userService.save(dummyUserDTO);

			assertThat(user).isNotNull();
			assertThat(user).isEqualTo(dummyUser);
			assertThat(user.getPassword()).isEqualTo("encodedPassword");

			verify(userRepository, times(1)).findByUsername(eq(dummyUserDTO.getUsername()));
			verify(userRepository, times(1)).save(eq(dummyUser));
			verifyNoMoreInteractions(userRepository);
		}

		@Test
		@DisplayName("Should NOT save a user if already exists")
		public void givenUserAlreadyExists_whenSaveUser_thenThrowException() {
			when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(dummyUser));

			RuntimeException exception = assertThrows(UserAlreadyExistsException.class, () -> {
				userService.save(dummyUserDTO);
			});

			String expectedExceptionMessage = String.format("User '%s' already exists", dummyUser.getUsername());

			assertThat(exception.getMessage()).isEqualTo(expectedExceptionMessage);

			verify(userRepository, times(1)).findByUsername(dummyUser.getUsername());
			verifyNoMoreInteractions(userRepository);
		}
	}

	@Nested
	@DisplayName("update() Tests")
	class UpdateTests {
		private UserDTO dummyUserDTO;

		@BeforeEach
		void setUp() {
			dummyUserDTO = new UserDTO();
			dummyUserDTO.setUsername("j.smith");
			dummyUserDTO.setFullname("Jane Smith");
			dummyUserDTO.setPassword("NeWval1dP@ssword");
			dummyUserDTO.setRole("USER");

			dummyUser.setId(1);
			dummyUser.setPassword("encodedPassword");
		}

		@Test
		@DisplayName("Should update a user if username NOT already exists")
		public void givenNewUsernameNotExists_whenUpdateUser_thenUpdateUser() {
			User newUser = new User();
			newUser.setId(dummyUser.getId());
			newUser.setUsername("j.smith");
			newUser.setFullname("Jane Smith");
			newUser.setPassword("encodedPassword");
			newUser.setRole(dummyUser.getRole());

			when(userRepository.findById(anyInt())).thenReturn(Optional.of(dummyUser));
			when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
			when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
			when(userRepository.save(any(User.class))).thenReturn(newUser);

			User user = userService.update(1, dummyUserDTO);

			assertThat(user).isNotNull();
			assertThat(user).isEqualTo(newUser);

			verify(userRepository, times(1)).findById(eq(1));
			verify(userRepository, times(1)).findByUsername(eq(dummyUserDTO.getUsername()));
			verify(userRepository, times(1)).save(eq(newUser));
			verifyNoMoreInteractions(userRepository);
		}

		@Test
		@DisplayName("Should NOT update a user if username already exists")
		public void givenUserAlreadyExists_whenUpdateUser_thenThrowException() {
			User existingUser = new User();
			existingUser.setId(2);
			existingUser.setUsername("j.smith");
			existingUser.setFullname("Jane Smith");
			existingUser.setPassword("val1dP@ssword");
			existingUser.setRole("USER");

			when(userRepository.findById(anyInt())).thenReturn(Optional.of(dummyUser));
			when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));

			RuntimeException exception = assertThrows(UserAlreadyExistsException.class, () -> {
				userService.update(1, dummyUserDTO);
			});

			String expectedExceptionMessage = String.format("User '%s' already exists", dummyUserDTO.getUsername());

			assertThat(exception.getMessage()).isEqualTo(expectedExceptionMessage);

			verify(userRepository, times(1)).findById(eq(1));
			verify(userRepository, times(1)).findByUsername(dummyUserDTO.getUsername());
			verifyNoMoreInteractions(userRepository);
		}
	}

	@Nested
	@DisplayName("delete() Tests")
	class DeleteTests {
		@Test
		@DisplayName("Should delete a user if exists")
		public void givenUserExists_whenDeleteUser_thenDeleteUser() {
			when(userRepository.existsById(anyInt())).thenReturn(true);

			userService.delete(1);

			verify(userRepository, times(1)).existsById(eq(1));
			verify(userRepository, times(1)).deleteById(eq(1));
			verifyNoMoreInteractions(userRepository);
		}

		@Test
		@DisplayName("Should NOT delete a user if NOT exists")
		public void givenUserNotExists_whenDeleteUser_thenThrowException() {
			when(userRepository.existsById(anyInt())).thenReturn(false);

			RuntimeException exception = assertThrows(UserIdNotFoundException.class, () -> {
				userService.delete(1);
			});

			String expectedExceptionMessage = String.format("User with Id '%s' doesn't exist.", 1);

			assertThat(exception.getMessage()).isEqualTo(expectedExceptionMessage);

			verify(userRepository, times(1)).existsById(eq(1));
			verifyNoMoreInteractions(userRepository);
		}
	}
}
