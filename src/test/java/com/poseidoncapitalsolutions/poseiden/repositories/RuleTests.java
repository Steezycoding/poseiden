package com.poseidoncapitalsolutions.poseiden.repositories;

import com.poseidoncapitalsolutions.poseiden.domain.RuleName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RuleTests {

	@Autowired
	private RuleNameRepository ruleNameRepository;

	@Test
	public void ruleTest() {
		RuleName rule = new RuleName(null, "Rule Name", "Description", "{\"field\": \"JSON Value\"}", "Template 1", "SELECT * FROM table", "WHERE id = 1");

		// Save
		rule = ruleNameRepository.save(rule);
		assertNotNull(rule.getId());
		assertTrue(rule.getName().equals("Rule Name"));

		// Update
		rule.setDescription("Description Updated");
		rule = ruleNameRepository.save(rule);
		assertTrue(rule.getDescription().equals("Description Updated"));

		// Find
		List<RuleName> listResult = ruleNameRepository.findAll();
		assertTrue(listResult.size() > 0);

		// Find by id
		Optional<RuleName> optional = ruleNameRepository.findById(rule.getId());
		assertTrue(optional.isPresent());

		// Delete
		ruleNameRepository.deleteById(rule.getId());
		Optional<RuleName> deletedTrade = ruleNameRepository.findById(rule.getId());
		assertThat(deletedTrade).isNotPresent();
	}
}
