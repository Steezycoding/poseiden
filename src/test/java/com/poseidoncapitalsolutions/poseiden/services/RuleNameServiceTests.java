package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.RuleNameDTO;
import com.poseidoncapitalsolutions.poseiden.domain.RuleName;
import com.poseidoncapitalsolutions.poseiden.repositories.RuleNameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
	@DisplayName("getAll() Tests")
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

	@Nested
	@DisplayName("save() Tests")
	class SaveRuleNameTests {
		private RuleNameDTO dummyRuleNameDTO;

		@BeforeEach
		public void setUp() {
			dummyRuleNameDTO = new RuleNameDTO();
			dummyRuleNameDTO.setName("Rule 1");
			dummyRuleNameDTO.setDescription("Description");
			dummyRuleNameDTO.setJson("{\"field\": \"JSON Value\"}");
			dummyRuleNameDTO.setTemplate("Template 1");
			dummyRuleNameDTO.setSqlStr("SELECT * FROM table");
			dummyRuleNameDTO.setSqlPart("WHERE id = 1");

			dummyRuleName.setId(null);
		}

		@Test
		@DisplayName("Should save a rulename")
		public void shouldSaveRuleName() {
			when(ruleNameRepository.save(any(RuleName.class))).thenReturn(dummyRuleName);

			RuleName result = ruleNameService.save(dummyRuleNameDTO);

			assertThat(result).isNotNull();
			assertThat(result).isEqualTo(dummyRuleName);

			verify(ruleNameRepository, times(1)).save(dummyRuleName);
			verifyNoMoreInteractions(ruleNameRepository);
		}
	}

	@Nested
	@DisplayName("getById() Tests")
	class GetRuleNameByIdTests {
		@Test
		@DisplayName("Should return a rulename by id")
		public void shouldReturnRuleNameById() {
			when(ruleNameRepository.findById(anyInt())).thenReturn(java.util.Optional.of(dummyRuleName));

			RuleName result = ruleNameService.getById(1);

			assertThat(result).isNotNull();
			assertThat(result).isEqualTo(dummyRuleName);

			verify(ruleNameRepository, times(1)).findById(1);
			verifyNoMoreInteractions(ruleNameRepository);
		}

		@Test
		@DisplayName("Should throw EntityNotFoundException when getting a rulename that does not exist")
		public void shouldThrowEntityNotFoundExceptionWhenGettingRuleNameThatDoesNotExist() {
			Integer ruleNameId = 1;
			when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());

			RuntimeException exception = assertThrows(EntityNotFoundException.class, () -> {
				ruleNameService.getById(ruleNameId);
			});

			String expectedExceptionMessage = String.format("RuleName with id %s not found", ruleNameId);

			assertThat(exception.getMessage()).isEqualTo(expectedExceptionMessage);

			verify(ruleNameRepository, times(1)).findById(1);
			verifyNoMoreInteractions(ruleNameRepository);
		}
	}

	@Nested
	@DisplayName("update() Tests")
	class UpdateRuleNameTests {
		private RuleNameDTO dummyRuleNameDTO;

		@BeforeEach
		public void setUp() {
			dummyRuleNameDTO = new RuleNameDTO();
			dummyRuleNameDTO.setId(1);
			dummyRuleNameDTO.setName("Rule 1");
			dummyRuleNameDTO.setDescription("Description");
			dummyRuleNameDTO.setJson("{\"field\": \"JSON Value\"}");
			dummyRuleNameDTO.setTemplate("Template 1");
			dummyRuleNameDTO.setSqlStr("SELECT * FROM table");
			dummyRuleNameDTO.setSqlPart("WHERE id = 1");
		}

		@Test
		@DisplayName("Should update a rulename")
		public void shouldUpdateRuleName() {
			when(ruleNameRepository.save(any(RuleName.class))).thenReturn(dummyRuleName);

			RuleName result = ruleNameService.update(dummyRuleNameDTO);

			assertThat(result).isNotNull();
			assertThat(result).isEqualTo(dummyRuleName);

			verify(ruleNameRepository, times(1)).save(dummyRuleName);
			verifyNoMoreInteractions(ruleNameRepository);
		}
	}
}
