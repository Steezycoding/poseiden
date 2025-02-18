package com.poseidoncapitalsolutions.poseiden.controller;

import com.poseidoncapitalsolutions.poseiden.controllers.LoginController;
import com.poseidoncapitalsolutions.poseiden.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class LoginControllerTests {
	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@InjectMocks
	private LoginController loginController;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
	}

	@Test
	@DisplayName("GET /app/login : Should return the 'login-view' view")
	public void shouldReturnTheLoginView() throws Exception {
		mockMvc.perform(get("/app/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("login-view"));
	}

	@Test
	@DisplayName("GET /app/login-redirect : Should redirect ADMIN user to /user/list")
	public void testRedirectAfterLogin_Admin() throws Exception {
		Authentication authentication = new TestingAuthenticationToken(
				"admin",
				null,
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
		);

		mockMvc.perform(get("/app/login-redirect").principal(authentication))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/list"));
	}

	@Test
	@DisplayName("GET /app/login-redirect : Should redirect USER user to /bidList/list")
	public void testRedirectAfterLogin_User() throws Exception {
		Authentication authentication = new TestingAuthenticationToken(
				"user",
				null,
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
		);

		mockMvc.perform(get("/app/login-redirect").principal(authentication))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/bidList/list"));
	}

	@Test
	@DisplayName("GET /app/login-redirect : Should redirect ANONYMOUS user to /app/login")
	public void testRedirectAfterLogin_Anonymous() throws Exception {
		Authentication authentication = new TestingAuthenticationToken(
				"anonymous",
				null,
				Collections.emptyList()
		);

		mockMvc.perform(get("/app/login-redirect").principal(authentication))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/app/login"));
	}

	@Test
	@DisplayName("GET /app/error : Should return the '403' view with error message and username")
	public void shouldReturnThe403ViewWithErrorMessageAndUsername() throws Exception {
		mockMvc.perform(get("/app/error").principal(new TestingAuthenticationToken("user", null)))
				.andExpect(status().isOk())
				.andExpect(view().name("403"))
				.andExpect(model().attributeExists("errorMsg"))
				.andExpect(model().attribute("username", "user"));
	}

	@Test
	@DisplayName("GET /app/error : Should return the '403' view with error message and no username")
	public void shouldReturnThe403ViewWithErrorMessageAndNoUsername() throws Exception {
		mockMvc.perform(get("/app/error"))
				.andExpect(status().isOk())
				.andExpect(view().name("403"))
				.andExpect(model().attributeExists("errorMsg"))
				.andExpect(model().attributeDoesNotExist("username"));
	}
}
