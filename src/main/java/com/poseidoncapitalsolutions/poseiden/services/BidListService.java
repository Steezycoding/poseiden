package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.domain.BidList;
import com.poseidoncapitalsolutions.poseiden.repositories.BidListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BidListService {
	private final BidListRepository bidListRepository;

	public BidListService(BidListRepository bidListRepository) {
		this.bidListRepository = bidListRepository;
	}

	public List<BidList> getAll() {
		return bidListRepository.findAll();
	}
}
