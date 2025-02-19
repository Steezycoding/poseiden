package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.domain.RuleName;
import com.poseidoncapitalsolutions.poseiden.repositories.RuleNameRepository;
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
}
