package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.domain.RuleName;
import com.poseidoncapitalsolutions.poseiden.repositories.RuleNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RuleNameServiceTests {
	@Mock
	private RuleNameRepository ruleNameRepository;

	@InjectMocks
	private RuleNameService ruleNameService;

	private RuleName dummyRuleName;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		dummyRuleName = new RuleName();
		dummyRuleName.setId(1);
		dummyRuleName.setName("Rule 1");
		dummyRuleName.setDescription("Description");
		dummyRuleName.setJson("{\"field\": \"JSON Value\"}");
		dummyRuleName.setTemplate("Template 1");
		dummyRuleName.setSqlStr("SELECT * FROM table");
		dummyRuleName.setSqlPart("WHERE id = 1");
	}

	@Nested
	class GetAllRuleNamesTests {
		@Test
		@DisplayName("Should return a list of rulenames")
		public void shouldReturnListOfRuleNames() {
			RuleName dummyRuleName2 = new RuleName();
			dummyRuleName2.setId(2);
			dummyRuleName2.setName("Rule 2");
			dummyRuleName2.setDescription("Description");
			dummyRuleName2.setJson("{\"field\": \"JSON Value\"}");
			dummyRuleName2.setTemplate("Template 1");
			dummyRuleName2.setSqlStr("SELECT * FROM table");
			dummyRuleName2.setSqlPart("WHERE id = 2");

			List<RuleName> list = List.of(dummyRuleName, dummyRuleName2);

			when(ruleNameRepository.findAll()).thenReturn(list);

			List<RuleName> result = ruleNameService.getAll();

			assertThat(result).hasSize(2);
			assertThat(result).contains(dummyRuleName, dummyRuleName2);

			verify(ruleNameRepository, times(1)).findAll();
			verifyNoMoreInteractions(ruleNameRepository);
		}
	}
}
