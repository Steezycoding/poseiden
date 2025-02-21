package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.TradeDTO;
import com.poseidoncapitalsolutions.poseiden.domain.Trade;
import com.poseidoncapitalsolutions.poseiden.repositories.TradeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TradeService {
	private final TradeRepository tradeRepository;

	public TradeService(TradeRepository tradeRepository) {
		this.tradeRepository = tradeRepository;
	}

	public List<Trade> getAll() {
		return tradeRepository.findAll();
	}

	public Trade getById(Integer id) {
		return tradeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Trade with id " + id + " not found"));
	}

	public Trade save(TradeDTO trade) {
		return tradeRepository.save(trade.toEntity());
	}

	public Trade update(TradeDTO tradeDTO) {
		return tradeRepository.save(tradeDTO.toEntity());
	}

	public void delete(Integer id) {
		if (tradeRepository.existsById(id)) {
			tradeRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("Trade with id " + id + " not found");
		}
	}
}
