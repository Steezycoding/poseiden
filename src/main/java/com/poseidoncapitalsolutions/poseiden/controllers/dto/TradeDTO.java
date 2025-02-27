package com.poseidoncapitalsolutions.poseiden.controllers.dto;

import com.poseidoncapitalsolutions.poseiden.domain.Trade;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDTO {
	private Integer id;

	@NotBlank(message = "Account is mandatory")
	private String account;

	@NotBlank(message = "Type is mandatory")
	private String type;

	@NotNull(message = "Buy Quantity is mandatory")
	@Min(value = 1, message = "Buy Quantity must be greater than 0")
	private Double buyQuantity;

	public Trade toEntity() {
		Trade trade = new Trade();
		trade.setId(id);
		trade.setAccount(account);
		trade.setType(type);
		trade.setBuyQuantity(buyQuantity);
		return trade;
	}

	public TradeDTO fromEntity(Trade trade) {
		this.id = trade.getId();
		this.account = trade.getAccount();
		this.type = trade.getType();
		this.buyQuantity = trade.getBuyQuantity();
		return this;
	}
}
