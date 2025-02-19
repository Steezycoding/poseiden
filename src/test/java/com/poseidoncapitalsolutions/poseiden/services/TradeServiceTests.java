package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.domain.Trade;
import com.poseidoncapitalsolutions.poseiden.repositories.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TradeServiceTests {
	@Mock
	private TradeRepository tradeRepository;

	@InjectMocks
	private TradeService tradeService;

	private Trade dummyTrade;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		dummyTrade = new Trade();
		dummyTrade.setId(1);
		dummyTrade.setAccount("Account 1");
		dummyTrade.setType("Trade Type 1");
		dummyTrade.setBuyQuantity(1.0);
	}

	@Nested
	@DisplayName("getAll() Tests")
	class GetAllRatingsTests {
		@Test
		@DisplayName("Should return a list of all ratings")
		public void shouldReturnListOfRatings() {
			Trade dummyTrade2 = new Trade();
			dummyTrade2.setId(2);
			dummyTrade2.setAccount("Account 2");
			dummyTrade2.setType("Trade Type 2");
			dummyTrade2.setBuyQuantity(2.0);

			List<Trade> list = List.of(dummyTrade, dummyTrade2);

			when(tradeRepository.findAll()).thenReturn(list);

			List<Trade> result = tradeService.getAll();

			assertThat(result).hasSize(2);
			assertThat(result).contains(dummyTrade, dummyTrade2);

			verify(tradeRepository, times(1)).findAll();
			verifyNoMoreInteractions(tradeRepository);
		}
	}
}
