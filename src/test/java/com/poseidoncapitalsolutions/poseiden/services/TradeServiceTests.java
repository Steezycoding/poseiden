package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.TradeDTO;
import com.poseidoncapitalsolutions.poseiden.domain.Trade;
import com.poseidoncapitalsolutions.poseiden.repositories.TradeRepository;
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

	@Nested
	@DisplayName("save() Tests")
	class SaveTests {
		private TradeDTO dummyTradeDTO;

		@BeforeEach
		public void setUp() {
			dummyTradeDTO = new TradeDTO();
			dummyTradeDTO.setAccount("Account 1");
			dummyTradeDTO.setType("Trade Type 1");
			dummyTradeDTO.setBuyQuantity(1.0);

			dummyTrade.setId(null);
		}

		@Test
		@DisplayName("Should save a trade")
		public void shouldSaveTrade() {
			when(tradeRepository.save(any(Trade.class))).thenReturn(dummyTrade);

			Trade result = tradeService.save(dummyTradeDTO);

			assertThat(result).isNotNull();
			assertThat(result).isEqualTo(dummyTrade);

			verify(tradeRepository, times(1)).save(dummyTrade);
			verifyNoMoreInteractions(tradeRepository);
		}
	}

	@Nested
	@DisplayName("getById() Tests")
	class GetByIdTests {
		@Test
		@DisplayName("Should return a trade by id")
		public void shouldReturnTradeById() {
			when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(dummyTrade));

			Trade result = tradeService.getById(1);

			assertThat(result).isNotNull();
			assertThat(result).isEqualTo(dummyTrade);

			verify(tradeRepository, times(1)).findById(eq(1));
			verifyNoMoreInteractions(tradeRepository);
		}
	}

	@Nested
	@DisplayName("update() Tests")
	class UpdateTests {
		private TradeDTO updatedTradeDTO;

		@BeforeEach
		public void setUp() {
			updatedTradeDTO = new TradeDTO();
			updatedTradeDTO.setId(1);
			updatedTradeDTO.setAccount("Account 1");
			updatedTradeDTO.setType("Trade Type 2");
			updatedTradeDTO.setBuyQuantity(10.0);
		}

		@Test
		@DisplayName("Should update a trade")
		public void shouldUpdateTrade() {
			Trade newTradeEntity = updatedTradeDTO.toEntity();
			when(tradeRepository.save(any(Trade.class))).thenReturn(newTradeEntity);

			Trade result = tradeService.update(updatedTradeDTO);

			assertThat(result).isNotNull();
			assertThat(result).isEqualTo(newTradeEntity);

			verify(tradeRepository, times(1)).save(eq(newTradeEntity));
			verifyNoMoreInteractions(tradeRepository);
		}
	}

	@Nested
	@DisplayName("delete() Tests")
	class DeleteTests {
		private final Integer tradeId = 1;

		@Test
		@DisplayName("Should delete a trade by id")
		public void shouldDeleteTradeById() {
			when(tradeRepository.existsById(anyInt())).thenReturn(true);

			tradeService.delete(tradeId);

			verify(tradeRepository, times(1)).existsById(eq(tradeId));
			verify(tradeRepository, times(1)).deleteById(eq(tradeId));
			verifyNoMoreInteractions(tradeRepository);
		}

		@Test
		@DisplayName("Should throw EntityNotFoundException when trying to delete a non-existing trade")
		public void shouldThrowEntityNotFoundExceptionWhenDeletingNonExistingTrade() {
			when(tradeRepository.existsById(anyInt())).thenReturn(false);

			RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
				tradeService.delete(tradeId);
			});

			String expectedExceptionMessage = String.format("Trade with id %s not found", tradeId);

			assertThat(exception.getMessage()).isEqualTo(expectedExceptionMessage);

			verify(tradeRepository, times(1)).existsById(eq(tradeId));
			verifyNoMoreInteractions(tradeRepository);
		}
	}
}
