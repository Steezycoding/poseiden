package com.poseidoncapitalsolutions.poseiden.controller;

import com.poseidoncapitalsolutions.poseiden.controllers.BidListController;
import com.poseidoncapitalsolutions.poseiden.domain.BidList;
import com.poseidoncapitalsolutions.poseiden.services.BidListService;
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
public class BidListControllerTests {
	private MockMvc mockMvc;

	@Mock
	private BidListService bidListService;

	@InjectMocks
	private BidListController bidListController;

	private BidList dummyBidList;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(bidListController).build();

		dummyBidList = new BidList("Account 1", "Type 1", 1d);
	}

	@Nested
	@DisplayName("/bidList/list Tests")
	class GetAllBidsTests {
		@Test
		@DisplayName("GET /bidList/list : Should return the 'bidList/list' view with a list of bids")
		public void getAllBidsTest_WithUser() throws Exception {
			BidList dummyBidList2 = new BidList("Account 2", "Type 2", 2d);
			List<BidList> list = List.of(
					dummyBidList,
					dummyBidList2
			);

			Authentication authentication = new TestingAuthenticationToken(
					"user",
					null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
			);

			when(bidListService.getAll()).thenReturn(list);

			mockMvc.perform(get("/bidList/list").principal(authentication))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("bidList/list"))
					.andExpect(model().attributeExists("bidLists"))
					.andExpect(model().attribute("bidLists", hasSize(2)))
					.andExpect(model().attribute("bidLists", contains(
							allOf(
									hasProperty("account", is(dummyBidList.getAccount())),
									hasProperty("type", is(dummyBidList.getType())),
									hasProperty("bidQuantity", is(dummyBidList.getBidQuantity()))
							),
							allOf(
									hasProperty("account", is(dummyBidList2.getAccount())),
									hasProperty("type", is(dummyBidList2.getType())),
									hasProperty("bidQuantity", is(dummyBidList2.getBidQuantity()))
							)
					)));

			verify(bidListService, times(1)).getAll();
			verifyNoMoreInteractions(bidListService);
		}
	}
}
