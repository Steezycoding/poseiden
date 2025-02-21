package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.RatingDTO;
import com.poseidoncapitalsolutions.poseiden.domain.Rating;
import com.poseidoncapitalsolutions.poseiden.repositories.RatingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RatingService {
	private final RatingRepository ratingRepository;

	public RatingService(RatingRepository ratingRepository) {
		this.ratingRepository = ratingRepository;
	}

	public List<Rating> getAll() {
		return ratingRepository.findAll();
	}

	public Rating getById(Integer id) {
		return ratingRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Rating with id " + id + " not found"));
	}

	public Rating save(RatingDTO ratingDTO) {
		return ratingRepository.save(ratingDTO.toEntity());
	}

	public Rating update(RatingDTO ratingDTO) {
		return ratingRepository.save(ratingDTO.toEntity());
	}

	public void delete(Integer id) {
		if (ratingRepository.existsById(id)) {
			ratingRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("Rating with id " + id + " not found");
		}
	}
}
