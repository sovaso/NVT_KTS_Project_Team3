package com.nvt.kts.team3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.repository.LeasedZoneRepository;
import com.nvt.kts.team3.service.LeasedZoneService;

@Service
public class LeasedZoneServiceImpl implements LeasedZoneService{

	@Autowired
	private LeasedZoneRepository leasedZoneRepository;
	
	@Override
	public LeasedZone findById(Long id) {
		return leasedZoneRepository.getOne(id);
	}

	@Override
	@Transactional
	public LeasedZone save(LeasedZone leasedZone) {
		return leasedZoneRepository.save(leasedZone);
	}

	@Override
	public List<LeasedZone> findAll() {
		return leasedZoneRepository.findAll();
	}

	@Override
	public void remove(Long id) {
		leasedZoneRepository.deleteById(id);
	}

	@Override
	@Transactional
	public LeasedZone saveAndFlush(LeasedZone leasedZone) {
		return leasedZoneRepository.saveAndFlush(leasedZone);
	}

	@Override
	public ArrayList<LeasedZone> getEventLeasedZones(long eventId) {
		return leasedZoneRepository.getEventLeasedZones(eventId);
	}

}