package com.poseidoncapitalsolutions.poseiden.controller;

import com.poseidoncapitalsolutions.poseiden.controllers.CurveController;
import com.poseidoncapitalsolutions.poseiden.controllers.dto.CurvePointDTO;
import com.poseidoncapitalsolutions.poseiden.domain.CurvePoint;
import com.poseidoncapitalsolutions.poseiden.services.CurvePointService;
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

	@Nested
	@DisplayName("/curvePoint/add Tests")
	class AddCurveTests {
		@Test
		@DisplayName("GET /curvePoint/add : Should return the 'curvePoint/add' view with an empty CurvePointDTO")
		public void addCurveTest() throws Exception {
			mockMvc.perform(get("/curvePoint/add"))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("curvePoint/add"))
					.andExpect(model().attributeExists("curvePoint"));
		}
	}

	@Nested
	@DisplayName("/curvePoint/validate Tests")
	class ValidateCurveTests {
		private CurvePointDTO dummyCurvePointDTO;

		@BeforeEach
		public void setUp() {
			dummyCurvePointDTO = new CurvePointDTO();
			dummyCurvePointDTO.setCurveId(10);
			dummyCurvePointDTO.setTerm(10d);
			dummyCurvePointDTO.setValue(30d);
		}

		@Test
		@DisplayName("POST /curvePoint/validate : Should save a curve point and redirect to /curvePoint/list")
		public void shouldValidateValidCurvePointAddForm() throws Exception {
			mockMvc.perform(post("/curvePoint/validate")
							.param("curveId", dummyCurvePointDTO.getCurveId().toString())
							.param("term", dummyCurvePointDTO.getTerm().toString())
							.param("value", dummyCurvePointDTO.getValue().toString()))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/curvePoint/list"));

			verify(curvePointService, times(1)).save(eq(dummyCurvePointDTO));
			verifyNoMoreInteractions(curvePointService);
		}

		@Test
		@DisplayName("POST /curvePoint/validate : Should NOT save the curvePoint with invalid collected form values")
		public void shouldNotValidateInvalidCurvePointAddForm() throws Exception {
			mockMvc.perform(post("/curvePoint/validate")
							.param("curveId", "")
							.param("term", "")
							.param("value", ""))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("curvePoint/add"));

			verifyNoInteractions(curvePointService);
		}
	}

	@Nested
	@DisplayName("/curvePoint/update/{id} Tests")
	class UpdateCurveTests {
		@BeforeEach
		public void setUp() {
			dummyCurvePoint.setId(1);
		}

		@Test
		@DisplayName("GET /curvePoint/update/{id} : Should return the 'curvePoint/update' view with the curve point to update")
		public void shouldReturnTheCurvePointUpdateView() throws Exception {
			when(curvePointService.getById(1)).thenReturn(dummyCurvePoint);

			mockMvc.perform(get("/curvePoint/update/1"))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("curvePoint/update"))
					.andExpect(model().attributeExists("curvePoint"))
					.andExpect(model().attribute("curvePoint", hasProperty("curveId", is(dummyCurvePoint.getCurveId()))))
					.andExpect(model().attribute("curvePoint", hasProperty("term", is(dummyCurvePoint.getTerm()))))
					.andExpect(model().attribute("curvePoint", hasProperty("value", is(dummyCurvePoint.getValue()))));

			verify(curvePointService, times(1)).getById(eq(1));
			verifyNoMoreInteractions(curvePointService);
		}

		@Test
		@DisplayName("GET /curvePoint/update/{id} : Should throw an exception when the curve point is not found")
		public void shouldThrowExceptionWhenCurvePointNotFound() throws Exception {
			doThrow(new EntityNotFoundException("CurvePoint with id 1 not found")).when(curvePointService).getById(1);

			mockMvc.perform(get("/curvePoint/update/1"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/curvePoint/list"));

			verify(curvePointService, times(1)).getById(1);
			verifyNoMoreInteractions(curvePointService);
		}

		@Test
		@DisplayName("POST /curvePoint/update/{id} : Should update the curve point and redirect to /curvePoint/list")
		public void shouldUpdateTheCurvePoint() throws Exception {
			CurvePointDTO dummyCurvePointDTO = new CurvePointDTO().fromEntity(dummyCurvePoint);
			mockMvc.perform(post("/curvePoint/update/1")
							.param("curveId", dummyCurvePointDTO.getCurveId().toString())
							.param("term", dummyCurvePointDTO.getTerm().toString())
							.param("value", dummyCurvePointDTO.getValue().toString()))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/curvePoint/list"));

			verify(curvePointService, times(1)).update(eq(dummyCurvePointDTO));
			verifyNoMoreInteractions(curvePointService);
		}

		@Test
		@DisplayName("POST /curvePoint/update/{id} : Should NOT update the curve point with invalid collected form values")
		public void shouldNotUpdateInvalidCurvePointForm() throws Exception {
			mockMvc.perform(post("/curvePoint/update/1")
							.param("curveId", "")
							.param("term", "")
							.param("value", ""))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("curvePoint/update"));

			verifyNoInteractions(curvePointService);
		}
	}
}
