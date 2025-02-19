package com.poseidoncapitalsolutions.poseiden.controller;

import com.poseidoncapitalsolutions.poseiden.controllers.RatingController;
import com.poseidoncapitalsolutions.poseiden.domain.Rating;
import com.poseidoncapitalsolutions.poseiden.services.RatingService;
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
}