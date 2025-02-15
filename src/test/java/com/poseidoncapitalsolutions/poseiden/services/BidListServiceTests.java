package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.domain.BidList;
import com.poseidoncapitalsolutions.poseiden.repositories.BidListRepository;
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
}
