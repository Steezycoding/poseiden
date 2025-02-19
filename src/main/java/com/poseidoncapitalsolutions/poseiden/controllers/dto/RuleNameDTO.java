package com.poseidoncapitalsolutions.poseiden.controllers.dto;

import com.poseidoncapitalsolutions.poseiden.domain.RuleName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleNameDTO {
	private Integer id;

	@NotBlank(message = "Name is mandatory")
	private String name;
	@NotBlank(message = "Description is mandatory")
	private String description;
	@NotBlank(message = "JSON is mandatory")
	private String json;
	@NotBlank(message = "Template is mandatory")
	private String template;
	@NotBlank(message = "SQL String is mandatory")
	private String sqlStr;
	@NotBlank(message = "SQL Part is mandatory")
	private String sqlPart;

	public RuleName toEntity() {
		RuleName ruleName = new RuleName();
		ruleName.setId(id);
		ruleName.setName(name);
		ruleName.setDescription(description);
		ruleName.setJson(json);
		ruleName.setTemplate(template);
		ruleName.setSqlStr(sqlStr);
		ruleName.setSqlPart(sqlPart);
		return ruleName;
	}
}
