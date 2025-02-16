package com.poseidoncapitalsolutions.poseiden.exceptions;

public class UserIdNotFoundException extends UserException {
	public UserIdNotFoundException(Integer id) {
		super(String.format("User with Id '%s' doesn't exist.", id));
	}
}
