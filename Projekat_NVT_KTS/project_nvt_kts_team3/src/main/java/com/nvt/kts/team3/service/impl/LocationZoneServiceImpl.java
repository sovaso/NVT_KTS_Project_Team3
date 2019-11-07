package com.nvt.kts.team3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.repository.LocationZoneRepository;
import com.nvt.kts.team3.service.LocationZoneService;

@Service
public class LocationZoneServiceImpl implements LocationZoneService{

	@Autowired
	private LocationZoneRepository locationZoneRepository;
	
	@Override
	public LocationZone findById(Long id) {
		return locationZoneRepository.getOne(id);
	}

	@Override
	public LocationZone save(LocationZone locationZone) {
		return locationZoneRepository.save(locationZone);
	}

	@Override
	public List<LocationZone> findAll() {
		return locationZoneRepository.findAll();
	}

	@Override
	public void remove(Long id) {
		locationZoneRepository.deleteById(id);
	}

	@Override
	public ArrayList<Maintenance> getActiveMaintenances(long locationZoneId) {
		return locationZoneRepository.getActiveMaintenances(locationZoneId);
	}

}
