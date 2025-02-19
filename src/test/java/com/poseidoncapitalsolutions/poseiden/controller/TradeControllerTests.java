package com.poseidoncapitalsolutions.poseiden.controller;

import com.poseidoncapitalsolutions.poseiden.controllers.TradeController;
import com.poseidoncapitalsolutions.poseiden.controllers.dto.TradeDTO;
import com.poseidoncapitalsolutions.poseiden.domain.Trade;
import com.poseidoncapitalsolutions.poseiden.services.TradeService;
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
public class TradeControllerTests {
	private MockMvc mockMvc;

	@Mock
	private TradeService tradeService;

	@InjectMocks
	private TradeController tradeController;

	private Trade dummyTrade;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();
		dummyTrade = new Trade();
		dummyTrade.setId(1);
		dummyTrade.setAccount("Account 1");
		dummyTrade.setType("Trade Type 1");
		dummyTrade.setBuyQuantity(1.0);
	}

	@Nested
	@DisplayName("/trade/list Tests")
	class GetAllRatingsTests {
		@Test
		@DisplayName("GET /trade/list : Should return the 'trade/list' view with a list of trades")
		public void getAllRatingsTest_WithUser() throws Exception {
			Trade dummyTrade2 = new Trade();
			dummyTrade2.setId(2);
			dummyTrade2.setAccount("Account 2");
			dummyTrade2.setType("Trade Type 2");
			dummyTrade2.setBuyQuantity(2.0);

			List<Trade> list = List.of(dummyTrade, dummyTrade2);

			Authentication authentication = new TestingAuthenticationToken(
					"user",
					null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
			);

			when(tradeService.getAll()).thenReturn(list);

			mockMvc.perform(get("/trade/list").principal(authentication))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("trade/list"))
					.andExpect(model().attributeExists("trades"))
					.andExpect(model().attribute("trades", hasSize(2)))
					.andExpect(model().attribute("trades", contains(
							allOf(
									hasProperty("id", is(dummyTrade.getId())),
									hasProperty("account", is(dummyTrade.getAccount())),
									hasProperty("type", is(dummyTrade.getType())),
									hasProperty("buyQuantity", is(dummyTrade.getBuyQuantity()))
							),
							allOf(
									hasProperty("id", is(dummyTrade2.getId())),
									hasProperty("account", is(dummyTrade2.getAccount())),
									hasProperty("type", is(dummyTrade2.getType())),
									hasProperty("buyQuantity", is(dummyTrade2.getBuyQuantity()))
							)
					)));

			verify(tradeService, times(1)).getAll();
			verifyNoMoreInteractions(tradeService);
		}
	}

	@Nested
	@DisplayName("/trade/add Tests")
	class AddTradeTests {
		@Test
		@DisplayName("GET /trade/add : Should return the 'trade/add' view with a new empty form")
		public void addTradeTest_WithUser() throws Exception {
			Authentication authentication = new TestingAuthenticationToken(
					"user",
					null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
			);

			mockMvc.perform(get("/trade/add").principal(authentication))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("trade/add"))
					.andExpect(model().attributeExists("trade"));

			verifyNoInteractions(tradeService);
		}
	}

	@Nested
	@DisplayName("/trade/validate Tests")
	class ValidateTradeTests {
		private TradeDTO dummyTradeDTO;

		@BeforeEach
		public void setUp() {
			dummyTradeDTO = new TradeDTO();
			dummyTradeDTO.setAccount("Account 1");
			dummyTradeDTO.setType("Trade Type 1");
			dummyTradeDTO.setBuyQuantity(1.0);
		}

		@Test
		@DisplayName("POST /trade/validate : Should save a trade and redirect to /trade/list")
		public void shouldValidateValidRatingAddForm() throws Exception {
			mockMvc.perform(post("/trade/validate")
							.param("account", dummyTradeDTO.getAccount())
							.param("type", dummyTradeDTO.getType())
							.param("buyQuantity", String.valueOf(dummyTradeDTO.getBuyQuantity())))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/trade/list"));

			verify(tradeService, times(1)).save(eq(dummyTradeDTO));
			verifyNoMoreInteractions(tradeService);
		}

		@Test
		@DisplayName("POST /trade/validate : Should return the 'trade/add' view with errors when the form is invalid")
		public void shouldReturnTradeAddViewWithErrors_WhenTradeAddFormIsInvalid() throws Exception {
			mockMvc.perform(post("/trade/validate")
							.param("account", "")
							.param("type", "")
							.param("buyQuantity", ""))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("trade/add"))
					.andExpect(model().attributeHasFieldErrors("trade", "account"))
					.andExpect(model().attributeHasFieldErrors("trade", "type"))
					.andExpect(model().attributeHasFieldErrors("trade", "buyQuantity"));

			verifyNoInteractions(tradeService);
		}
	}
}
