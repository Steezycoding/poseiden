package com.poseidoncapitalsolutions.poseiden.repositories;

import com.poseidoncapitalsolutions.poseiden.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

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

		// Find
		List<User> listResult = repository.findAll();
		assertThat(listResult).isNotEmpty();
		assertThat(listResult).hasSize(1);
	}
}
