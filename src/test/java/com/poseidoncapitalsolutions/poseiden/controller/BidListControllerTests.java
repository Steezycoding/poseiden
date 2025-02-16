package com.poseidoncapitalsolutions.poseiden.controller;

import com.poseidoncapitalsolutions.poseiden.controllers.BidListController;
import com.poseidoncapitalsolutions.poseiden.controllers.dto.BidListDTO;
import com.poseidoncapitalsolutions.poseiden.domain.BidList;
import com.poseidoncapitalsolutions.poseiden.services.BidListService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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

	@Nested
	@DisplayName("/bidList/add Tests")
	class AddBidTests {
		@Test
		@DisplayName("GET /bidList/add : Should return the 'bidList/add' view")
		public void shouldReturnTheBidListAddView() throws Exception {
			mockMvc.perform(get("/bidList/add"))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("bidList/add"))
					.andExpect(model().attributeExists("bid"));

			verifyNoInteractions(bidListService);
		}
	}

	@Nested
	@DisplayName("/bidList/validate Tests")
	class ValidateBidTests {
		private BidListDTO dummyValidBidListDTO;

		@BeforeEach
		public void setUp() {
			dummyValidBidListDTO = new BidListDTO();
			dummyValidBidListDTO.setAccount("Account 1");
			dummyValidBidListDTO.setType("Type 1");
			dummyValidBidListDTO.setBidQuantity(1d);
		}

		@Test
		@DisplayName("POST /bidList/validate : Should return the 'bidList/add' view")
		public void shouldValidateValidBidListForm() throws Exception {
			mockMvc.perform(post("/bidList/validate")
							.param("account", dummyValidBidListDTO.getAccount())
							.param("type", dummyValidBidListDTO.getType())
							.param("bidQuantity", String.valueOf(dummyValidBidListDTO.getBidQuantity())))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/bidList/list"));

			verify(bidListService, times(1)).save(eq(dummyValidBidListDTO));
			verifyNoMoreInteractions(bidListService);
		}

		@Test
		@DisplayName("POST /bidList/validate : Should NOT save the bid with invalid collected form values")
		public void shouldNotValidateInvalidBidListForm() throws Exception {
			mockMvc.perform(post("/bidList/validate")
							.param("account", "")
							.param("type", "")
							.param("bidQuantity", "0"))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("bidList/add"));

			verifyNoInteractions(bidListService);
		}
	}

	@Nested
	@DisplayName("/bidList/update/{id} Tests")
	class UpdateBidTests {
		@BeforeEach
		public void setUp() {
			dummyBidList.setId(1);
		}

		@Test
		@DisplayName("GET /bidList/update/{id} : Should return the 'bidList/update' view with the bid to update")
		public void shouldReturnTheBidListUpdateView() throws Exception {
			when(bidListService.getById(anyInt())).thenReturn(dummyBidList);

			mockMvc.perform(get("/bidList/update/1"))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("bidList/update"))
					.andExpect(model().attributeExists("bid"))
					.andExpect(model().attribute("bid", hasProperty("account", is(dummyBidList.getAccount()))))
					.andExpect(model().attribute("bid", hasProperty("type", is(dummyBidList.getType()))))
					.andExpect(model().attribute("bid", hasProperty("bidQuantity", is(dummyBidList.getBidQuantity()))));

			verify(bidListService, times(1)).getById(1);
			verifyNoMoreInteractions(bidListService);
		}

		@Test
		@DisplayName("GET /bidList/update/{id} : Should throw an exception when the bid is not found")
		public void shouldThrowExceptionWhenBidNotFound() throws Exception {
			doThrow(new EntityNotFoundException("BidList with id 1 not found")).when(bidListService).getById(1);

			mockMvc.perform(get("/bidList/update/1"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/bidList/list"));

			verify(bidListService, times(1)).getById(1);
			verifyNoMoreInteractions(bidListService);
		}

		@Test
		@DisplayName("POST /bidList/update/{id} : Should update a bid")
		public void shouldUpdateTheBid() throws Exception {
			BidListDTO dummyBidListDTO = new BidListDTO().fromEntity(dummyBidList);
			when(bidListService.update(ArgumentMatchers.any(BidListDTO.class))).thenReturn(dummyBidList);

			mockMvc.perform(post("/bidList/update/1")
							.param("account", dummyBidListDTO.getAccount())
							.param("type", dummyBidListDTO.getType())
							.param("bidQuantity", String.valueOf(dummyBidListDTO.getBidQuantity())))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/bidList/list"));

			verify(bidListService, times(1)).update(eq(dummyBidListDTO));
			verifyNoMoreInteractions(bidListService);
		}

		@Test
		@DisplayName("POST /bidList/update/{id} : Should NOT update a bid with invalid collected form values")
		public void shouldNotUpdateInvalidBidListForm() throws Exception {
			mockMvc.perform(post("/bidList/update/1")
							.param("account", "")
							.param("type", "")
							.param("bidQuantity", "0"))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("bidList/update"));

			verifyNoInteractions(bidListService);
		}
	}
}
