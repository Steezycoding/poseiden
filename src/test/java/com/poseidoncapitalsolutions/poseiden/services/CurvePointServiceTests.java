package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.CurvePointDTO;
import com.poseidoncapitalsolutions.poseiden.domain.CurvePoint;
import com.poseidoncapitalsolutions.poseiden.repositories.CurvePointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurvePointServiceTests {
	@Mock
	private CurvePointRepository curvePointRepository;

	@InjectMocks
	private CurvePointService curvePointService;

	private CurvePoint dummyCurvePoint;

	@BeforeEach
	void setUp() {
		dummyCurvePoint = new CurvePoint(10, 10d, 30d);
	}

	@Nested
	@DisplayName("getAll() Tests")
	class GetAllTests {
		@Test
		@DisplayName("Should return all curve points")
		public void getAllTest() {
			CurvePoint dummyCurvePoint2 = new CurvePoint(22, 50d, 50d);

			when(curvePointRepository.findAll()).thenReturn(List.of(dummyCurvePoint, dummyCurvePoint2));

			List<CurvePoint> curvePoints = curvePointService.getAll();

			assertThat(curvePoints).hasSize(2);
			assertThat(curvePoints).contains(dummyCurvePoint, dummyCurvePoint2);

			verify(curvePointRepository, times(1)).findAll();
			verifyNoMoreInteractions(curvePointRepository);
		}
	}

	@Nested
	@DisplayName("save() Tests")
	class SaveTests {
		private CurvePointDTO dummyCurvePointDTO;

		@BeforeEach
		void setUp() {
			dummyCurvePointDTO = new CurvePointDTO();
			dummyCurvePointDTO.setCurveId(10);
			dummyCurvePointDTO.setTerm(10d);
			dummyCurvePointDTO.setValue(30d);
		}

		@Test
		@DisplayName("Should save a curve point")
		public void saveTest() {
			when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(dummyCurvePoint);

			CurvePoint curvePoint = curvePointService.save(dummyCurvePointDTO);

			assertThat(curvePoint).isNotNull();
			assertThat(curvePoint).isEqualTo(dummyCurvePoint);

			verify(curvePointRepository, times(1)).save(eq(dummyCurvePoint));
			verifyNoMoreInteractions(curvePointRepository);
		}
	}
}
