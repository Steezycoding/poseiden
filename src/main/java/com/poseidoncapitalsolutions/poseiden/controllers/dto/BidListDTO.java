package com.poseidoncapitalsolutions.poseiden.controllers.dto;

import com.poseidoncapitalsolutions.poseiden.domain.BidList;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidListDTO {
	private Integer id;

	@NotBlank(message = "Account is mandatory")
	private String account;

	@NotBlank(message = "Type is mandatory")
	private String type;

	@NotNull(message = "Bid Quantity is mandatory")
	@Min(value = 1, message = "Minimum value is 1")
	private Double bidQuantity;

	public BidList toEntity() {
		BidList bidList = new BidList();
		bidList.setId(id);
		bidList.setAccount(account);
		bidList.setType(type);
		bidList.setBidQuantity(bidQuantity);
		return bidList;
	}

	public BidListDTO fromEntity(BidList bidList) {
		this.id = bidList.getId();
		this.account = bidList.getAccount();
		this.type = bidList.getType();
		this.bidQuantity = bidList.getBidQuantity();
		return this;
	}
}
