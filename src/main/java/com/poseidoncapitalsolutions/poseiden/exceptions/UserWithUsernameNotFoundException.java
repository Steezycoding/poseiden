package com.poseidoncapitalsolutions.poseiden.exceptions;

public class UserWithUsernameNotFoundException extends UserException {
	public UserWithUsernameNotFoundException(String username) {
		super(String.format("User with username %s not found", username));
	}
}
