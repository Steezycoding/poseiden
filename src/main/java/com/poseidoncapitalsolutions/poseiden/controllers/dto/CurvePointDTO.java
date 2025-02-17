package com.poseidoncapitalsolutions.poseiden.controllers.dto;

import com.poseidoncapitalsolutions.poseiden.domain.CurvePoint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurvePointDTO {
	private Integer id;

	@NotNull(message = "Curve Id is mandatory")
	private Integer curveId;

	@NotNull(message = "Term is mandatory")
	private Double term;

	@NotNull(message = "Value is mandatory")
	private Double value;

	public CurvePoint toEntity() {
		CurvePoint curvePoint = new CurvePoint();
		curvePoint.setId(id);
		curvePoint.setCurveId(curveId);
		curvePoint.setTerm(term);
		curvePoint.setValue(value);
		return curvePoint;
	}

	public CurvePointDTO fromEntity(CurvePoint curvePoint) {
		this.id = curvePoint.getId();
		this.curveId = curvePoint.getCurveId();
		this.term = curvePoint.getTerm();
		this.value = curvePoint.getValue();
		return this;
	}
}
