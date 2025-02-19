package com.poseidoncapitalsolutions.poseiden.controllers.dto;

import com.poseidoncapitalsolutions.poseiden.domain.Rating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
	private Integer id;

	@NotBlank(message = "Moodys Rating is mandatory")
	private String moodysRating;

	@NotBlank(message = "S&P Rating is mandatory")
	private String sandPRating;

	@NotBlank(message = "Fitch Rating is mandatory")
	private String fitchRating;

	@NotNull(message = "Order Number is mandatory")
	private Integer orderNumber;

	public Rating toEntity() {
		Rating rating = new Rating();
		rating.setId(id);
		rating.setMoodysRating(moodysRating);
		rating.setSandPRating(sandPRating);
		rating.setFitchRating(fitchRating);
		rating.setOrderNumber(orderNumber);
		return rating;
	}
}
