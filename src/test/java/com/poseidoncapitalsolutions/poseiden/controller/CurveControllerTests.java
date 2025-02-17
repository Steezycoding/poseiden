package com.poseidoncapitalsolutions.poseiden.controller;

import com.poseidoncapitalsolutions.poseiden.controllers.CurveController;
import com.poseidoncapitalsolutions.poseiden.domain.CurvePoint;
import com.poseidoncapitalsolutions.poseiden.services.CurvePointService;
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
public class CurveControllerTests {
	private MockMvc mockMvc;

	@Mock
	private CurvePointService curvePointService;

	@InjectMocks
	private CurveController curveController;

	private CurvePoint dummyCurvePoint;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(curveController).build();

		dummyCurvePoint = new CurvePoint(10, 10d, 30d);
	}

	@Nested
	@DisplayName("/curvePoint/list Tests")
	class GetAllCurveTests {
		@Test
		@DisplayName("GET /curvePoint/list : Should return the 'curvePoint/list' view with a list of curves")
		public void getAllBidsTest_WithUser() throws Exception {
			CurvePoint dummyCurvePoint2 = new CurvePoint(22, 50d, 50d);
			List<CurvePoint> list = List.of(
					dummyCurvePoint,
					dummyCurvePoint2
			);

			Authentication authentication = new TestingAuthenticationToken(
					"user",
					null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
			);

			when(curvePointService.getAll()).thenReturn(list);

			mockMvc.perform(get("/curvePoint/list").principal(authentication))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("curvePoint/list"))
					.andExpect(model().attributeExists("curvePoints"))
					.andExpect(model().attribute("curvePoints", hasSize(2)))
					.andExpect(model().attribute("curvePoints", contains(
							allOf(
									hasProperty("curveId", is(dummyCurvePoint.getCurveId())),
									hasProperty("term", is(dummyCurvePoint.getTerm())),
									hasProperty("value", is(dummyCurvePoint.getValue()))
							),
							allOf(
									hasProperty("curveId", is(dummyCurvePoint2.getCurveId())),
									hasProperty("term", is(dummyCurvePoint2.getTerm())),
									hasProperty("value", is(dummyCurvePoint2.getValue()))
							)
					)));

			verify(curvePointService, times(1)).getAll();
			verifyNoMoreInteractions(curvePointService);
		}
	}
}
