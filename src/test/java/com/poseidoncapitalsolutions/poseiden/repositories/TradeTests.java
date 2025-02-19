package com.poseidoncapitalsolutions.poseiden.repositories;

import com.poseidoncapitalsolutions.poseiden.domain.Trade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeTests {

	@Autowired
	private TradeRepository tradeRepository;

	@Test
	public void tradeTest() {
		Trade trade = new Trade();
		trade.setId(null);
		trade.setAccount("Trade Account");
		trade.setType("Trade Type");
		trade.setBuyQuantity(1.0);

		// Save
		trade = tradeRepository.save(trade);
		assertNotNull(trade.getId());
		assertTrue(trade.getAccount().equals("Trade Account"));

		// Update
		trade.setAccount("Trade Account Updated");
		trade = tradeRepository.save(trade);
		assertTrue(trade.getAccount().equals("Trade Account Updated"));

		// Find
		List<Trade> listResult = tradeRepository.findAll();
		assertTrue(listResult.size() > 0);

		// Find by id
		Optional<Trade> optional = tradeRepository.findById(trade.getId());
		assertTrue(optional.isPresent());
	}
}
