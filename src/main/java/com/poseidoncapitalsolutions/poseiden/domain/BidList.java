package com.poseidoncapitalsolutions.poseiden.domain;

// import org.springframework.beans.factory.annotation.Required;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
public class BidList {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;
}
