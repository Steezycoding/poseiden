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
@Table(name = "curvepoint")
public class CurvePoint {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	@Column(name="curve_id") private Integer curveId;
	@Column(name="as_of_date") private Timestamp asOfDate;
	private Double term;
	private Double value;
	@Column(name="creation_date") private Timestamp creationDate;

	public CurvePoint(Integer curveId, Double term, Double value) {
		this.curveId = curveId;
		this.term = term;
		this.value = value;
	}
}
