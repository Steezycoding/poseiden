package com.poseidoncapitalsolutions.poseiden.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordConfig {

	/**
	 * Creates a BCryptPasswordEncoder bean that is used to encode passwords
	 * @return BCryptPasswordEncoder object that is used to encode passwords
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
