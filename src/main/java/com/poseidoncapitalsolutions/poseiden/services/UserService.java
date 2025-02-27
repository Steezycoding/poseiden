package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.UserDTO;
import com.poseidoncapitalsolutions.poseiden.domain.User;
import com.poseidoncapitalsolutions.poseiden.exceptions.UserAlreadyExistsException;
import com.poseidoncapitalsolutions.poseiden.exceptions.UserIdNotFoundException;
import com.poseidoncapitalsolutions.poseiden.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}

	public User getById(Integer id) {
		return userRepository.findById(id).stream()
				.findFirst()
				.orElseThrow(() -> new UserIdNotFoundException(id));
	}

	public User save(UserDTO userDTO) {
		if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
			throw new UserAlreadyExistsException(userDTO.getUsername());
		}

		String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
		userDTO.setPassword(encodedPassword);

		return userRepository.save(userDTO.toEntity());
	}

	public User update(Integer id, UserDTO userDTO) {
		User user = this.getById(id);

		if (!user.getUsername().equals(userDTO.getUsername())) {
			User existingUser = getByUsername(userDTO.getUsername());
			if (existingUser != null && !existingUser.getId().equals(id)) {
				throw new UserAlreadyExistsException(userDTO.getUsername());
			}
			user.setUsername(userDTO.getUsername());
		}

		user.setFullname(userDTO.getFullname());

		if (userDTO.getPassword() != null
				&& !userDTO.getPassword().isEmpty()
				&& !passwordEncoder.matches(userDTO.getPassword(), user.getPassword()))
		{
			user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		}

		user.setRole(userDTO.getRole());

		return userRepository.save(user);
	}

	public void delete(Integer id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
		} else {
			throw new UserIdNotFoundException(id);
		}
	}

	public User getByUsername(String username) {
		return userRepository.findByUsername(username)
				.stream()
				.findFirst()
				.orElse(null);
	}
}
