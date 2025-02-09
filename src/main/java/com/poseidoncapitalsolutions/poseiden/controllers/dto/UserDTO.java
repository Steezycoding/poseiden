package com.poseidoncapitalsolutions.poseiden.controllers.dto;

import com.poseidoncapitalsolutions.poseiden.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	@NotBlank(message = "Username is mandatory")
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9._-]*$", message = "Username must be alphanumeric and begin with letter.")
	@Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters.")
	private String username;

	@NotBlank(message = "Password is mandatory")
	@Pattern(regexp = "^(?! )[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]*(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).*$", message = "Password must contain at least one letter, one number, one special character and never begin with space.")
	@Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters long.")
	private String password;

	@NotBlank(message = "FullName is mandatory")
	private String fullname;

	@NotBlank(message = "Role is mandatory")
	@Pattern(regexp = "^[A-Z]+$", message = "Role must be in uppercase")
	private String role;

	public UserDTO fromEntity(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.fullname = user.getFullname();
		this.role = user.getRole();
		return this;
	}

	public User toEntity() {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setFullname(fullname);
		user.setRole(role);
		return user;
	}
}
