package com.poseidoncapitalsolutions.poseiden.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bidlist")
public class BidList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Account is mandatory")
	private String account;

	@NotBlank(message = "Type is mandatory")
	private String type;

	@Column(name = "bid_quantity") public Double bidQuantity;
	@Column(name = "ask_quantity") public Double askQuantity;
	private Double bid;
	private Double ask;
	private String benchmark;
	@Column(name = "bid_list_date") private Timestamp bidListDate;
	private String commentary;
	private String security;
	private String status;
	private String trader;
	private String book;
	@Column(name = "creation_name") private String creationName;
	@Column(name = "creation_date") private Timestamp creationDate;
	@Column(name = "revision_name") public String revisionName;
	@Column(name = "revision_date") public Timestamp revisionDate;
	@Column(name = "deal_name") public String dealName;
	@Column(name = "deal_type")public String dealType;
	@Column(name = "source_list_id")public String sourceListId;
	public String side;

	public BidList(String account, String type, Double bidQuantity) {
		this.account = account;
		this.type = type;
		this.bidQuantity = bidQuantity;
	}
}
