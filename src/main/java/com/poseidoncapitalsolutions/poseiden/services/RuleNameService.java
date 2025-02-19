package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.RuleNameDTO;
import com.poseidoncapitalsolutions.poseiden.domain.RuleName;
import com.poseidoncapitalsolutions.poseiden.repositories.RuleNameRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RuleNameService {
	private final RuleNameRepository ruleNameRepository;

	public RuleNameService(RuleNameRepository ruleNameRepository) {
		this.ruleNameRepository = ruleNameRepository;
	}

	public List<RuleName> getAll() {
		return ruleNameRepository.findAll();
	}

	public RuleName getById(Integer id) {
		return ruleNameRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("RuleName with id " + id + " not found"));
	}

	public RuleName save(RuleNameDTO ruleNameDTO) {
		return ruleNameRepository.save(ruleNameDTO.toEntity());
	}

	public RuleName update(RuleNameDTO ruleNameDTO) {
		return ruleNameRepository.save(ruleNameDTO.toEntity());
	}

	public void delete(Integer id) {
		if (ruleNameRepository.existsById(id)) {
			ruleNameRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("RuleName with id " + id + " not found");
		}
	}
}
