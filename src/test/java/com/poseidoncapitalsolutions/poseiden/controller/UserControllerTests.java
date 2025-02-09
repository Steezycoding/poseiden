package com.poseidoncapitalsolutions.poseiden.controller;

import com.poseidoncapitalsolutions.poseiden.controllers.UserController;
import com.poseidoncapitalsolutions.poseiden.controllers.dto.UserDTO;
import com.poseidoncapitalsolutions.poseiden.domain.User;
import com.poseidoncapitalsolutions.poseiden.exceptions.UserIdNotFoundException;
import com.poseidoncapitalsolutions.poseiden.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class UserControllerTests {
	private MockMvc mockMvc;

	private final UserService userService = Mockito.mock(UserService.class);

	private User dummyValidUser;

	@BeforeEach
	public void setUp() {
		UserController userController = new UserController(userService);
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

		dummyValidUser = new User();
		dummyValidUser.setUsername("j.doe");
		dummyValidUser.setFullname("John Doe");
		dummyValidUser.setPassword("val1dP@ssword");
		dummyValidUser.setRole("USER");
	}

	@Test
	@DisplayName("GET /user/list : Should return the 'user/list' view with a list of users")
	public void shouldReturnTheUserListView() throws Exception {
		User dummyValidUser2 = new User();
		dummyValidUser2.setUsername("j.smith");
		dummyValidUser2.setFullname("Jane Smith");
		dummyValidUser2.setPassword("val1dP@ssword");
		dummyValidUser2.setRole("USER");

		when(userService.getAll()).thenReturn(List.of(dummyValidUser, dummyValidUser2));

		mockMvc.perform(get("/user/list"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("user/list"))
				.andExpect(model().attributeExists("users"))
				.andExpect(model().attribute("users", hasSize(2)))
				.andExpect(model().attribute("users", contains(
						allOf(
								hasProperty("username", is(dummyValidUser.getUsername())),
								hasProperty("fullname", is(dummyValidUser.getFullname())),
								hasProperty("password", is(dummyValidUser.getPassword())),
								hasProperty("role", is(dummyValidUser.getRole()))
						),
						allOf(
								hasProperty("username", is(dummyValidUser2.getUsername())),
								hasProperty("fullname", is(dummyValidUser2.getFullname())),
								hasProperty("password", is(dummyValidUser2.getPassword())),
								hasProperty("role", is(dummyValidUser2.getRole()))
						)
				)));

		verify(userService, times(1)).getAll();
		verifyNoMoreInteractions(userService);
	}

	@Test
	@DisplayName("GET /user/add : Should return the 'user/add' view with a new user form")
	public void shouldReturnTheUserAddView() throws Exception {
		mockMvc.perform(get("/user/add"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("user/add"))
				.andExpect(model().attributeExists("user"));

		verifyNoInteractions(userService);
	}

	@Nested
	@DisplayName("/user/validate Tests")
	class UserValidateTests {
		private UserDTO dummyValidUserDTO;

		@BeforeEach
		public void setUp() {
			dummyValidUserDTO = new UserDTO("j.doe", "val1dP@ssword", "John Doe", "USER");
		}

		@Test
		@DisplayName("POST /user/validate : Should save the user with valid collected form values")
		public void shouldValidateValidUserForm() throws Exception {
			mockMvc.perform(post("/user/validate")
							.param("username", dummyValidUserDTO.getUsername())
							.param("fullname", dummyValidUserDTO.getFullname())
							.param("password", dummyValidUserDTO.getPassword())
							.param("role", dummyValidUserDTO.getRole()))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/user/list"));

			verify(userService, times(1)).save(eq(dummyValidUserDTO));
			verifyNoMoreInteractions(userService);
		}

		@Test
		@DisplayName("POST /user/validate : Should NOT save the user with invalid collected form values")
		public void shouldNotValidateInvalidUserForm() throws Exception {
			mockMvc.perform(post("/user/validate")
							.param("username", "j")
							.param("fullname", "")
							.param("password", "invalidpassord")
							.param("role", ""))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("user/add"))
					.andExpect(model().attributeHasFieldErrors("user", "username", "password", "fullname", "role"));

			verifyNoInteractions(userService);
		}
	}

	@Nested
	@DisplayName("/user/update/{id} Tests")
	class UserUpdateTests {
		@Test
		@DisplayName("GET /user/update/{id} : Should return the 'user/update' view with the user to update")
		public void shouldReturnTheUserUpdateView() throws Exception {
			when(userService.getById(anyInt())).thenReturn(dummyValidUser);

			mockMvc.perform(get("/user/update/1"))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("user/update"))
					.andExpect(model().attributeExists("user"))
					.andExpect(model().attribute("user", hasProperty("username", is(dummyValidUser.getUsername()))))
					.andExpect(model().attribute("user", hasProperty("fullname", is(dummyValidUser.getFullname()))))
					.andExpect(model().attribute("user", hasProperty("password", is(dummyValidUser.getPassword()))))
					.andExpect(model().attribute("user", hasProperty("role", is(dummyValidUser.getRole()))));

			verify(userService, times(1)).getById(1);
			verifyNoMoreInteractions(userService);
		}

		@Test
		@DisplayName("GET /user/update/{id} : Should throw an exception when the user is not found")
		public void shouldThrowExceptionWhenUserNotFound() throws Exception {
			doThrow(new UserIdNotFoundException(1)).when(userService).getById(1);

			mockMvc.perform(get("/user/update/1"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/user/list"));

			verify(userService, times(1)).getById(1);
			verifyNoMoreInteractions(userService);
		}
	}
}
