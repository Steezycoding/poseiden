package com.poseidoncapitalsolutions.poseiden.repositories;

import com.poseidoncapitalsolutions.poseiden.domain.BidList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BidTests {

	@Autowired
	private BidListRepository bidListRepository;

	@Test
	public void bidListTest() {
		BidList bid = new BidList("Account Test", "Type Test", 10d);

		// Save
		bid = bidListRepository.save(bid);
		assertNotNull(bid.getId());
		assertEquals(bid.getBidQuantity(), 10d, 10d);

		// Find
		List<BidList> listResult = bidListRepository.findAll();
		assertTrue(listResult.size() > 0);
	}
}
