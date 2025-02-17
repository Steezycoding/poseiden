package com.poseidoncapitalsolutions.poseiden.config.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncodeTests {
	@Test
	public void testPassword() {
		String rawPassword = "123456";
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		String encodedPassword = encoder.encode(rawPassword);
		boolean matches = encoder.matches(rawPassword, encodedPassword);

		assertThat(matches).isTrue();
	}
}
