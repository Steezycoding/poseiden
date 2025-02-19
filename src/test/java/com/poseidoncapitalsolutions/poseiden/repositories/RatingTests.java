package com.poseidoncapitalsolutions.poseiden.repositories;

import com.poseidoncapitalsolutions.poseiden.domain.Rating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RatingTests {

	@Autowired
	private RatingRepository ratingRepository;

	@Test
	public void ratingTest() {
		Rating rating = new Rating(null,"Aaa", "AA", "A+", 10);

		// Save
		rating = ratingRepository.save(rating);
		assertNotNull(rating.getId());
		assertTrue(rating.getOrderNumber() == 10);

		// Update
		rating.setOrderNumber(20);
		rating = ratingRepository.save(rating);
		assertTrue(rating.getOrderNumber() == 20);

		// Find
		List<Rating> listResult = ratingRepository.findAll();
		assertTrue(listResult.size() > 0);

		// FingById
		Optional<Rating> optional = ratingRepository.findById(rating.getId());
		assertTrue(optional.isPresent());

		// Delete
		ratingRepository.delete(rating);
		Optional<Rating> deletedRating = ratingRepository.findById(rating.getId());
		assertThat(deletedRating).isNotPresent();

	}
}
