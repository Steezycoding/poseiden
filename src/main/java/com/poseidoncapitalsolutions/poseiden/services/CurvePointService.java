package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.CurvePointDTO;
import com.poseidoncapitalsolutions.poseiden.domain.CurvePoint;
import com.poseidoncapitalsolutions.poseiden.repositories.CurvePointRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CurvePointService {
	private final CurvePointRepository curvePointRepository;

	public CurvePointService(CurvePointRepository curvePointRepository) {
		this.curvePointRepository = curvePointRepository;
	}

	public List<CurvePoint> getAll() {
		return curvePointRepository.findAll();
	}

	public CurvePoint getById(Integer id) {
		return curvePointRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("CurvePoint with id " + id + " not found"));
	}

	public CurvePoint save(CurvePointDTO curvePointDTO) {
		return curvePointRepository.save(curvePointDTO.toEntity());
	}

	public CurvePoint update(CurvePointDTO curvePointDTO) {
		return curvePointRepository.save(curvePointDTO.toEntity());
	}

	public void delete(Integer id) {
		if (curvePointRepository.existsById(id)) {
			curvePointRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("CurvePoint with id " + id + " not found");
		}
	}
}
