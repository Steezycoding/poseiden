package com.poseidoncapitalsolutions.poseiden.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "rating")
public class Rating {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;
}
