package com.poseidoncapitalsolutions.poseiden.controller;

import com.poseidoncapitalsolutions.poseiden.controllers.RuleNameController;
import com.poseidoncapitalsolutions.poseiden.controllers.dto.RuleNameDTO;
import com.poseidoncapitalsolutions.poseiden.domain.RuleName;
import com.poseidoncapitalsolutions.poseiden.services.RuleNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class RuleNameControllerTests {
	private MockMvc mockMvc;

	@Mock
	private RuleNameService ruleNameService;

	@InjectMocks
	private RuleNameController ruleNameController;

	private RuleName dummyRuleName;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(ruleNameController).build();

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
	@DisplayName("/ruleName/list Tests")
	class GetAllRulenamesTests {
		@Test
		@DisplayName("GET /ruleName/list : Should return the 'rulename/list' view with a list of rulenames")
		public void getAllRulnames_WithUser() throws Exception {
			RuleName dummyRuleName2 = new RuleName(2, "Rule 1", "Description", "{\"field\": \"JSON Value\"}", "Template 1", "SELECT * FROM table", "WHERE id = 1");
			List<RuleName> list = List.of(dummyRuleName, dummyRuleName2);

			Authentication authentication = new TestingAuthenticationToken(
					"user",
					null,
					Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
			);

			when(ruleNameService.getAll()).thenReturn(list);

			mockMvc.perform(get("/ruleName/list").principal(authentication))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("ruleName/list"))
					.andExpect(model().attributeExists("ruleNames"))
					.andExpect(model().attribute("ruleNames", hasSize(2)))
					.andExpect(model().attribute("ruleNames", contains(
							allOf(
									hasProperty("id", is(dummyRuleName.getId())),
									hasProperty("name", is(dummyRuleName.getName())),
									hasProperty("description", is(dummyRuleName.getDescription())),
									hasProperty("json", is(dummyRuleName.getJson())),
									hasProperty("template", is(dummyRuleName.getTemplate())),
									hasProperty("sqlStr", is(dummyRuleName.getSqlStr())),
									hasProperty("sqlPart", is(dummyRuleName.getSqlPart()))
							),
							allOf(
									hasProperty("id", is(dummyRuleName2.getId())),
									hasProperty("name", is(dummyRuleName2.getName())),
									hasProperty("description", is(dummyRuleName2.getDescription())),
									hasProperty("json", is(dummyRuleName2.getJson())),
									hasProperty("template", is(dummyRuleName2.getTemplate())),
									hasProperty("sqlStr", is(dummyRuleName2.getSqlStr())),
									hasProperty("sqlPart", is(dummyRuleName2.getSqlPart()))
							)
					)));

			verify(ruleNameService, times(1)).getAll();
			verifyNoMoreInteractions(ruleNameService);
		}
	}

	@Nested
	@DisplayName("/ruleName/add Tests")
	class AddRuleNameFormTests {
		@Test
		@DisplayName("GET /ruleName/add : Should return the 'rulename/add' view with a new RuleNameDTO")
		public void addRuleForm() throws Exception {
			mockMvc.perform(get("/ruleName/add"))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("ruleName/add"))
					.andExpect(model().attributeExists("ruleName"));
		}
	}

	@Nested
	@DisplayName("/ruleName/validate Tests")
	class ValidateRuleNameTests {
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
		}

		@Test
		@DisplayName("POST /ruleName/validate : Should save a rule and redirect to /ruleName/list")
		public void shouldValidateValidRulenameAddForm() throws Exception {
			mockMvc.perform(post("/ruleName/validate")
					.param("name", dummyRuleNameDTO.getName())
					.param("description", dummyRuleNameDTO.getDescription())
					.param("json", dummyRuleNameDTO.getJson())
					.param("template", dummyRuleNameDTO.getTemplate())
					.param("sqlStr", dummyRuleNameDTO.getSqlStr())
					.param("sqlPart", dummyRuleNameDTO.getSqlPart()))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/ruleName/list"));

			verify(ruleNameService, times(1)).save(eq(dummyRuleNameDTO));
			verifyNoMoreInteractions(ruleNameService);
		}

		@Test
		@DisplayName("POST /ruleName/validate : Should return the 'rulename/add' view with errors when RuleNameDTO is invalid")
		public void validateRuleName_WithErrors() throws Exception {
			mockMvc.perform(post("/ruleName/validate")
					.param("name", "")
					.param("description", "")
					.param("json", "")
					.param("template", "")
					.param("sqlStr", "")
					.param("sqlPart", ""))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("ruleName/add"))
					.andExpect(model().attributeHasFieldErrors("ruleName", "name"))
					.andExpect(model().attributeHasFieldErrors("ruleName", "description"))
					.andExpect(model().attributeHasFieldErrors("ruleName", "json"))
					.andExpect(model().attributeHasFieldErrors("ruleName", "template"))
					.andExpect(model().attributeHasFieldErrors("ruleName", "sqlStr"))
					.andExpect(model().attributeHasFieldErrors("ruleName", "sqlPart"));

			verifyNoInteractions(ruleNameService);
		}
	}


}
