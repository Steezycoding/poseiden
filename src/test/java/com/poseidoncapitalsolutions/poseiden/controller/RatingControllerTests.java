package com.poseidoncapitalsolutions.poseiden.controller;

import com.poseidoncapitalsolutions.poseiden.controllers.RatingController;
import com.poseidoncapitalsolutions.poseiden.controllers.dto.RatingDTO;
import com.poseidoncapitalsolutions.poseiden.domain.Rating;
import com.poseidoncapitalsolutions.poseiden.services.RatingService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class RatingControllerTests {
	private MockMvc mockMvc;

	@Mock
	private RatingService ratingService;

	@InjectMocks
	private RatingController ratingController;

	private Rating dummyRating;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
		dummyRating = new Rating(1, "Aaa", "AA", "A+", 10);
	}

	@Nested
	@DisplayName("/rating/list Tests")
	class GetAllRatingsTests {
		@Test
		@DisplayName("GET /rating/list : Should return the 'rating/list' view with a list of ratings")
		public void getAllRatingsTest_WithUser() throws Exception {
			Rating dummyRating2 = new Rating(2, "Baa3", "BBB+", "BBB+", 22);
			List<Rating> list = List.of(dummyRating, dummyRating2);

			Authentication authentication = new TestingAuthenticationToken(
					"user",
					null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
			);

			when(ratingService.getAll()).thenReturn(list);

			mockMvc.perform(get("/rating/list").principal(authentication))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("rating/list"))
					.andExpect(model().attributeExists("ratings"))
					.andExpect(model().attribute("ratings", hasSize(2)))
					.andExpect(model().attribute("ratings", contains(
							allOf(
									hasProperty("id", is(dummyRating.getId())),
									hasProperty("moodysRating", is(dummyRating.getMoodysRating())),
									hasProperty("sandPRating", is(dummyRating.getSandPRating())),
									hasProperty("fitchRating", is(dummyRating.getFitchRating())),
									hasProperty("orderNumber", is(dummyRating.getOrderNumber()))
							),
							allOf(
									hasProperty("id", is(dummyRating2.getId())),
									hasProperty("moodysRating", is(dummyRating2.getMoodysRating())),
									hasProperty("sandPRating", is(dummyRating2.getSandPRating())),
									hasProperty("fitchRating", is(dummyRating2.getFitchRating())),
									hasProperty("orderNumber", is(dummyRating2.getOrderNumber()))
							)
					)));

			verify(ratingService, times(1)).getAll();
			verifyNoMoreInteractions(ratingService);
		}
	}

	@Nested
	@DisplayName("/rating/add Tests")
	class AddRatingTests {
		@Test
		@DisplayName("GET /rating/add : Should return the 'rating/add' view with an empty RatingDTO")
		public void addRatingTest() throws Exception {
			mockMvc.perform(get("/rating/add"))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("rating/add"))
					.andExpect(model().attributeExists("rating"));
		}
	}

	@Nested
	@DisplayName("/rating/validate Tests")
	class ValidateRatingTests {
		private RatingDTO dummyRatingDTO;

		@BeforeEach
		public void setUp() {
			dummyRatingDTO = new RatingDTO();
			dummyRatingDTO.setMoodysRating("Moodys A");
			dummyRatingDTO.setSandPRating("S&P A");
			dummyRatingDTO.setFitchRating("Fitch A");
			dummyRatingDTO.setOrderNumber(10);
		}

		@Test
		@DisplayName("POST /rating/validate : Should save a rating and redirect to /rating/list")
		public void shouldValidateValidRatingAddForm() throws Exception {
			mockMvc.perform(post("/rating/validate")
							.param("moodysRating", dummyRatingDTO.getMoodysRating())
							.param("sandPRating", dummyRatingDTO.getSandPRating())
							.param("fitchRating", dummyRatingDTO.getFitchRating())
							.param("orderNumber", dummyRatingDTO.getOrderNumber().toString()))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/rating/list"));

			verify(ratingService, times(1)).save(eq(dummyRatingDTO));
			verifyNoMoreInteractions(ratingService);
		}

		@Test
		@DisplayName("POST /rating/validate : Should NOT save the rating with invalid collected form values")
		public void shouldNotValidateInvalidRatingAddForm() throws Exception {
			mockMvc.perform(post("/rating/validate")
							.param("moodysRating", "")
							.param("sandPRating", "")
							.param("fitchRating", "")
							.param("orderNumber", ""))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("rating/add"));

			verifyNoInteractions(ratingService);
		}
	}

	@Nested
	@DisplayName("/rating/update/{id} Tests")
	class UpdateRatingTests {
		@Test
		@DisplayName("GET /rating/update/{id} : Should return the 'rating/update' view with the rating data")
		public void showRatingUpdateFormTest() throws Exception {
			when(ratingService.getById(anyInt())).thenReturn(dummyRating);

			mockMvc.perform(get("/rating/update/1"))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("rating/update"))
					.andExpect(model().attributeExists("rating"))
					.andExpect(model().attribute("rating", hasProperty("id", is(dummyRating.getId()))))
					.andExpect(model().attribute("rating", hasProperty("moodysRating", is(dummyRating.getMoodysRating()))))
					.andExpect(model().attribute("rating", hasProperty("sandPRating", is(dummyRating.getSandPRating()))))
					.andExpect(model().attribute("rating", hasProperty("fitchRating", is(dummyRating.getFitchRating()))))
					.andExpect(model().attribute("rating", hasProperty("orderNumber", is(dummyRating.getOrderNumber()))));

			verify(ratingService, times(1)).getById(1);
			verifyNoMoreInteractions(ratingService);
		}

		@Test
		@DisplayName("GET /rating/update/{id} : Should handle an exception when the rating is not found")
		public void shouldThrowExceptionWhenRatingNotFound() throws Exception {
			doThrow(new EntityNotFoundException("Rating with id 1 not found")).when(ratingService).getById(1);

			mockMvc.perform(get("/rating/update/1"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/rating/list"));

			verify(ratingService, times(1)).getById(1);
			verifyNoMoreInteractions(ratingService);
		}

		@Test
		@DisplayName("POST /rating/update/{id} : Should update the rating and redirect to /rating/list")
		public void shouldUpdateTheRating() throws Exception {
			RatingDTO ratingDTO = new RatingDTO().fromEntity(dummyRating);
			ratingDTO.setMoodysRating("Aa3");

			mockMvc.perform(post("/rating/update/1")
							.param("id", ratingDTO.getId().toString())
							.param("moodysRating", ratingDTO.getMoodysRating())
							.param("sandPRating", ratingDTO.getSandPRating())
							.param("fitchRating", ratingDTO.getFitchRating())
							.param("orderNumber", ratingDTO.getOrderNumber().toString()))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/rating/list"));

			verify(ratingService, times(1)).update(eq(ratingDTO));
			verifyNoMoreInteractions(ratingService);
		}

		@Test
		@DisplayName("POST /rating/update/{id} : Should NOT update the rating with invalid collected form values")
		public void shouldNotUpdateInvalidRatingForm() throws Exception {
			mockMvc.perform(post("/rating/update/1")
							.param("id", "")
							.param("moodysRating", "")
							.param("sandPRating", "")
							.param("fitchRating", "")
							.param("orderNumber", ""))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("rating/update"));

			verifyNoInteractions(ratingService);
		}
	}

	@Nested
	@DisplayName("/rating/delete/{id} Tests")
	class DeleteRatingTests {
		@Test
		@DisplayName("GET /rating/delete/{id} : Should delete the rating and redirect to /rating/list")
		public void shouldDeleteTheRating() throws Exception {
			mockMvc.perform(get("/rating/delete/1"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/rating/list"));

			verify(ratingService, times(1)).delete(eq(1));
			verifyNoMoreInteractions(ratingService);
		}

		@Test
		@DisplayName("GET /rating/delete/{id} : Should handle an exception when the rating is not found")
		public void shouldThrowExceptionWhenRatingNotFound() throws Exception {
			doThrow(new EntityNotFoundException("Rating with id 1 not found")).when(ratingService).delete(1);

			mockMvc.perform(get("/rating/delete/1"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/rating/list"));

			verify(ratingService, times(1)).delete(eq(1));
			verifyNoMoreInteractions(ratingService);
		}

		@Test
		@DisplayName("GET /rating/delete/{id} : Should throw an exception and redirect to list when the rating is not found")
		public void givenRatingNotExists_whenDelete_thenThrowExceptionAndRedirect() throws Exception {
			doThrow(new EntityNotFoundException("Rating with id 1 not found")).when(ratingService).delete(1);

			mockMvc.perform(get("/rating/delete/1"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/rating/list"));

			verify(ratingService, times(1)).delete(eq(1));
			verifyNoMoreInteractions(ratingService);
		}
	}
}