package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.RatingDTO;
import com.poseidoncapitalsolutions.poseiden.domain.Rating;
import com.poseidoncapitalsolutions.poseiden.repositories.RatingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RatingServiceTests {

	@Mock
	private RatingRepository ratingRepository;

	@InjectMocks
	private RatingService ratingService;

	private Rating dummyRating;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		dummyRating = new Rating(1, "Aaa", "AA", "A+", 10);
	}

	@Nested
	@DisplayName("getAll() Tests")
	class GetAllRatingsTests {
		@Test
		@DisplayName("Should return a list of ratings")
		public void shouldReturnListOfRatings() {
			Rating dummyRating2 = new Rating(2, "Baa3", "BBB+", "BBB+", 22);
			List<Rating> list = List.of(dummyRating, dummyRating2);

			when(ratingRepository.findAll()).thenReturn(list);

			List<Rating> result = ratingService.getAll();

			assertThat(result).hasSize(2);
			assertThat(result).contains(dummyRating, dummyRating2);

			verify(ratingRepository, times(1)).findAll();
			verifyNoMoreInteractions(ratingRepository);
		}
	}

	@Nested
	@DisplayName("save() Tests")
	class SaveRatingTests {
		private RatingDTO dummyRatingDTO;

		@BeforeEach
		public void setUp() {
			dummyRatingDTO = new RatingDTO();
			dummyRatingDTO.setMoodysRating("Aaa");
			dummyRatingDTO.setSandPRating("AA");
			dummyRatingDTO.setFitchRating("A+");
			dummyRatingDTO.setOrderNumber(10);

			dummyRating.setId(null);
		}

		@Test
		@DisplayName("Should save a rating")
		public void shouldSaveRating() {
			when(ratingRepository.save(any(Rating.class))).thenReturn(dummyRating);

			Rating rating = ratingService.save(dummyRatingDTO);

			assertThat(rating).isNotNull();
			assertThat(rating).isEqualTo(dummyRating);

			verify(ratingRepository, times(1)).save(eq(dummyRating));
			verifyNoMoreInteractions(ratingRepository);
		}
	}

	@Nested
	@DisplayName("getById() Tests")
	class GetRatingByIdTests {
		@Test
		@DisplayName("Should return a rating by id")
		public void shouldReturnRatingById() {
			when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(dummyRating));

			Rating result = ratingService.getById(1);

			assertThat(result).isNotNull();
			assertThat(result).isEqualTo(dummyRating);

			verify(ratingRepository, times(1)).findById(1);
			verifyNoMoreInteractions(ratingRepository);
		}

		@Test
		@DisplayName("Should throw EntityNotFoundException when rating with given ID does not exist")
		public void shouldRaiseExceptionIfRatingNotFound() {
			Integer ratingId = 1;
			when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());

			RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
				ratingService.getById(ratingId);
			});

			String expectedExceptionMessage = String.format("Rating with id %s not found", ratingId);

			assertThat(exception.getMessage()).isEqualTo(expectedExceptionMessage);

			verify(ratingRepository, times(1)).findById(eq(1));
			verifyNoMoreInteractions(ratingRepository);
		}
	}

	@Nested
	@DisplayName("update() Tests")
	class UpdateRatingTests {
		private RatingDTO dummyRatingDTO;

		@BeforeEach
		public void setUp() {
			dummyRatingDTO = new RatingDTO();
			dummyRatingDTO.setId(1);
			dummyRatingDTO.setMoodysRating("Aaa");
			dummyRatingDTO.setSandPRating("AA");
			dummyRatingDTO.setFitchRating("A+");
			dummyRatingDTO.setOrderNumber(10);
		}

		@Test
		@DisplayName("Should update a rating")
		public void shouldUpdateRating() {
			when(ratingRepository.save(any(Rating.class))).thenReturn(dummyRating);

			Rating result = ratingService.update(dummyRatingDTO);

			assertThat(result).isNotNull();
			assertThat(result).isEqualTo(dummyRating);

			verify(ratingRepository, times(1)).save(eq(dummyRating));
			verifyNoMoreInteractions(ratingRepository);
		}
	}
}