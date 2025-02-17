package com.poseidoncapitalsolutions.poseiden.services;

import com.poseidoncapitalsolutions.poseiden.controllers.dto.CurvePointDTO;
import com.poseidoncapitalsolutions.poseiden.domain.CurvePoint;
import com.poseidoncapitalsolutions.poseiden.repositories.CurvePointRepository;
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

	public CurvePoint save(CurvePointDTO curvePointDTO) {
		CurvePoint curvePoint = curvePointRepository.save(curvePointDTO.toEntity());
		return curvePoint;
	}
}
