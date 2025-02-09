package com.poseidoncapitalsolutions.poseiden.repositories;

import com.poseidoncapitalsolutions.poseiden.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTests {
	@Autowired
	private UserRepository repository;

	@Test
	public void userTest() {

		User dummyUser = new User();
		dummyUser.setUsername("j.doe");
		dummyUser.setFullname("John Doe");
		dummyUser.setPassword("val1dP@ssword");
		dummyUser.setRole("USER");

		// Save
		dummyUser = repository.save(dummyUser);
		assertThat(dummyUser).isNotNull();
		assertThat(dummyUser.getId()).isEqualTo(1);
		assertThat(dummyUser.getUsername()).isEqualTo("j.doe");

		// Update
		dummyUser.setFullname("Jane Doe");
		dummyUser = repository.save(dummyUser);
		assertThat(dummyUser.getFullname()).isEqualTo("Jane Doe");

		// Find
		List<User> listResult = repository.findAll();
		assertThat(listResult).isNotEmpty();
		assertThat(listResult).hasSize(1);

		// Find by id
		Optional<User> userById = repository.findById(dummyUser.getId());
		assertThat(userById).isPresent();

		// Find by username
		Optional<User> userByUsername = repository.findByUsername(dummyUser.getUsername());
		assertThat(userByUsername).isPresent();
	}
}
