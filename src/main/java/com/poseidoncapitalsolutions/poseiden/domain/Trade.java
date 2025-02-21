package com.poseidoncapitalsolutions.poseiden.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trade")
public class Trade {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	private String account;
	private String type;
	@Column(name = "buy_quantity") private Double buyQuantity;
	@Column(name = "sell_quantity") private Double sellQuantity;
	@Column(name = "buy_price") private Double buyPrice;
	@Column(name = "sell_price") private Double sellPrice;
	private String benchmark;
	@Column(name = "trade_date") private Timestamp tradeDate;
	private String security;
	private String status;
	private String trader;
	private String book;
	@Column(name = "creation_name") private String creationName;
	@Column(name = "creation_date") private Timestamp creationDate;
	@Column(name = "revision_name") private String revisionName;
	@Column(name = "revision_date") private Timestamp revisionDate;
	@Column(name = "deal_name") private String dealName;
	@Column(name = "deal_type") private String dealType;
	@Column(name = "source_list_id") private String sourceListId;
	private String side;
}
