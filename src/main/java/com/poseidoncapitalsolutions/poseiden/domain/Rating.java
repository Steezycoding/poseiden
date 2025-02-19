package com.poseidoncapitalsolutions.poseiden.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "moodys_rating")
	private String moodysRating;

	@Column(name = "sandp_rating")
	private String sandPRating;

	@Column(name = "fitch_rating")
	private String fitchRating;

	@Column(name = "order_number")
	private Integer orderNumber;
}
