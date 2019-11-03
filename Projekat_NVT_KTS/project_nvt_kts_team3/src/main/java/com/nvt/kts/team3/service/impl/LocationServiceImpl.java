package com.nvt.kts.team3.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.repository.LocationRepository;
import com.nvt.kts.team3.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService{

	@Autowired
	private LocationRepository locationRepository;
	
	@Override
	public Location findById(Long id) {
		return locationRepository.getOne(id);
	}

	@Override
	public Location save(Location location) {
		return locationRepository.save(location);
	}

	@Override
	public List<Location> findAll() {
		return locationRepository.findAll();
	}

	@Override
	public void remove(Long id) {
		locationRepository.deleteById(id);
	}

	@Override
	public Location findByNameAndAddress(String name, String address) {
		return locationRepository.findByNameAndAddress(name, address);
	}

	@Override
	public ArrayList<Event> getActiveEvents(long locationId) {
		return locationRepository.getActiveEvents(locationId);
	}

	@Override
	public ArrayList<Location> findAllActive() {
		return locationRepository.getActiveLocations();
	}

	@Override
	public ArrayList<Event> checkIfAvailable(Long locationId, Date startDate, Date endDate) {
		return locationRepository.checkIfAvailable(locationId, startDate, endDate);
	}

}
