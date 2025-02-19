package com.poseidoncapitalsolutions.poseiden.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rulename")
public class RuleName {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private String json;
	private String template;
	@Column(name = "sql_str") private String sqlStr;
	@Column(name = "sql_part") private String sqlPart;
}
