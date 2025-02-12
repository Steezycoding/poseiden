package com.poseidoncapitalsolutions.poseiden.exceptions;

public class UserAlreadyExistsException extends UserException {
	public UserAlreadyExistsException(String username) {
		super(String.format("User '%s' already exists", username));
	}
}
