package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.BidListDTO;
import com.poseidoncapitalsolutions.poseiden.domain.BidList;
import com.poseidoncapitalsolutions.poseiden.repositories.BidListRepository;
import jakarta.persistence.EntityNotFoundException;
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

	public BidList getById(Integer id) {
		return bidListRepository.findById(id).stream()
				.findFirst()
				.orElseThrow(() -> new EntityNotFoundException("BidList with id " + id + " not found"));
	}

	public BidList save(BidListDTO bid) {
		return bidListRepository.save(bid.toEntity());
	}

	public BidList update(BidListDTO bidListDTO) {
		return bidListRepository.save(bidListDTO.toEntity());
	}
}
