package com.poseidoncapitalsolutions.poseiden.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {
	private final CustomUserDetailsService customUserDetailsService;

	public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(auth -> auth
						.requestMatchers("/css/**").permitAll()
						.requestMatchers("/app/login").anonymous()
						.requestMatchers("/user/**", "/admin/**").hasRole("ADMIN")
						.anyRequest().authenticated())
				.formLogin((form) -> form
						.loginPage("/app/login")
						.defaultSuccessUrl("/app/login-redirect", true)
						.permitAll())
				.logout((logout) -> logout
						.logoutUrl("/app-logout")
						.logoutSuccessUrl("/login")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.clearAuthentication(true))
				.exceptionHandling(exception -> exception
						.accessDeniedPage("/app/error"))
				.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder
			bCryptPasswordEncoder) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
		return authenticationManagerBuilder.build();
	}
}
