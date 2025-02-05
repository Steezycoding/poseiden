package com.poseidoncapitalsolutions.poseiden.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;


@Entity
@Table(name = "trade")
public class Trade {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;
}
