package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.UserDTO;
import com.poseidoncapitalsolutions.poseiden.domain.User;
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
			when(userRepository.findByUsername(anyString())).thenReturn(null);
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
			when(userRepository.findByUsername(anyString())).thenReturn(dummyUser);

			RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> {
				userService.save(dummyUserDTO);
			});

			String expectedExceptionMessage = String.format("User '%s' already exists", dummyUser.getUsername());

			assertThat(exception.getMessage()).isEqualTo(expectedExceptionMessage);

			verify(userRepository, times(1)).findByUsername(dummyUser.getUsername());
			verifyNoMoreInteractions(userRepository);
		}
	}

}
