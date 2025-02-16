package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.BidListDTO;
import com.poseidoncapitalsolutions.poseiden.domain.BidList;
import com.poseidoncapitalsolutions.poseiden.repositories.BidListRepository;
import jakarta.persistence.EntityNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidListServiceTests {
	@Mock
	private BidListRepository bidListRepository;

	@InjectMocks
	private BidListService bidListService;

	private BidList dummyBidList;

	@BeforeEach
	void setUp() {
		dummyBidList = new BidList();
		dummyBidList.setAccount("Account 1");
		dummyBidList.setType("Type 1");
		dummyBidList.setBidQuantity(1d);
	}

	@Nested
	@DisplayName("getAll() Tests")
	class GetAllTests {
		@Test
		@DisplayName("Should return all bid lists")
		public void getAllTest() {
			BidList dummyBidList2 = new BidList();
			dummyBidList2.setAccount("Account 2");
			dummyBidList2.setType("Type 2");
			dummyBidList2.setBidQuantity(2d);

			when(bidListRepository.findAll()).thenReturn(List.of(dummyBidList, dummyBidList2));

			List<BidList> bidLists = bidListService.getAll();

			assertThat(bidLists).hasSize(2);
			assertThat(bidLists).contains(dummyBidList, dummyBidList2);

			verify(bidListRepository, times(1)).findAll();
			verifyNoMoreInteractions(bidListRepository);
		}
	}

	@Nested
	@DisplayName("save() Tests")
	class SaveTests {
		private BidListDTO validBidListDTO;

		@BeforeEach
		void setUp() {
			validBidListDTO = new BidListDTO();
			validBidListDTO.setAccount("Account 1");
			validBidListDTO.setType("Type 1");
			validBidListDTO.setBidQuantity(1d);
		}

		@Test
		@DisplayName("Should save a bid")
		public void saveTest() {
			when(bidListRepository.save(any(BidList.class))).thenReturn(dummyBidList);

			BidList bidList = bidListService.save(validBidListDTO);

			assertThat(bidList).isNotNull();
			assertThat(bidList).isEqualTo(dummyBidList);

			verify(bidListRepository, times(1)).save(any(BidList.class));
			verifyNoMoreInteractions(bidListRepository);
		}
	}

	@Nested
	@DisplayName("getById() Tests")
	class GetByIdTests {
		@Test
		@DisplayName("Should return a bid with the given ID")
		public void givenBidExists_whenGetById_thenReturnBid() {
			when(bidListRepository.findById(anyInt())).thenReturn(java.util.Optional.of(dummyBidList));

			BidList bidList = bidListService.getById(1);

			assertThat(bidList).isNotNull();
			assertThat(bidList).isEqualTo(dummyBidList);

			verify(bidListRepository, times(1)).findById(eq(1));
			verifyNoMoreInteractions(bidListRepository);
		}

		@Test
		@DisplayName("Should throw an exception if bid with the given ID does NOT exist")
		public void givenBidNotExists_whenGetById_thenThrowException() {
			Integer bidId = 1;
			when(bidListRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

			RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
				bidListService.getById(bidId);
			});

			String expectedExceptionMessage = String.format("BidList with id %s not found", bidId);

			assertThat(exception.getMessage()).isEqualTo(expectedExceptionMessage);

			verify(bidListRepository, times(1)).findById(bidId);
			verifyNoMoreInteractions(bidListRepository);
		}
	}

	@Nested
	@DisplayName("update() Tests")
	class UpdateTests {
		private BidListDTO validBidListDTO;

		@BeforeEach
		void setUp() {
			validBidListDTO = new BidListDTO(1, "Account 1", "Type 1", 1d);
			dummyBidList.setId(1);
		}

		@Test
		@DisplayName("Should update a bid")
		public void updateTest() {
			when(bidListRepository.save(any(BidList.class))).thenReturn(dummyBidList);

			BidList bidList = bidListService.update(validBidListDTO);

			assertThat(bidList).isNotNull();
			assertThat(bidList).isEqualTo(dummyBidList);

			verify(bidListRepository, times(1)).save(eq(dummyBidList));
			verifyNoMoreInteractions(bidListRepository);
		}
	}
}
