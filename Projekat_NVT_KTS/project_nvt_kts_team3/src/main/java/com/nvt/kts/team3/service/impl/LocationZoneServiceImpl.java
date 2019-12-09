package com.nvt.kts.team3.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.model.LeasedZone;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.repository.LocationZoneRepository;
import com.nvt.kts.team3.service.LocationService;
import com.nvt.kts.team3.service.LocationZoneService;

import exception.InvalidLocationZone;
import exception.LocationNotFound;
import exception.LocationZoneNotChangeable;
import exception.LocationZoneNotFound;

@Service
@Transactional(readOnly = true)
public class LocationZoneServiceImpl implements LocationZoneService {

	@Autowired
	private LocationZoneRepository locationZoneRepository;

	@Autowired
	private LocationService locationService;

	@Override
	public LocationZone findById(Long id) {
		return locationZoneRepository.findById(id).get();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public LocationZone save(LocationZoneDTO lz) {
		Location location = locationService.findById(lz.getLocationId());

		if (location == null || location.isStatus() == false) {
			throw new LocationNotFound();
		}
		LocationZone newZone = null;
		if (lz.isMatrix() && lz.getCol() > 0 && lz.getRow() > 0) {
			int capacity = lz.getCol() * lz.getRow();
			newZone = new LocationZone(lz.getRow(), lz.getName(), capacity, true, lz.getCol(),
					new HashSet<LeasedZone>(), location);
			location.getLocationZones().add(newZone);
		} else if (lz.isMatrix() == false && lz.getCapacity() > 0) {
			newZone = new LocationZone(0, lz.getName(), lz.getCapacity(), false, 0, new HashSet<LeasedZone>(),
					location);
			location.getLocationZones().add(newZone);
		} else {
			throw new InvalidLocationZone();
		}
		return locationZoneRepository.save(newZone);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public LocationZone update(LocationZoneDTO lz) {
		LocationZone zone = findById(lz.getId());

		if (zone == null || zone.getLocation().isStatus() == false) {
			throw new LocationZoneNotFound();
		}
		List<Maintenance> activeMaintenances = getActiveMaintenances(zone.getId());
		if (activeMaintenances != null && activeMaintenances.isEmpty() == false) {
			throw new LocationZoneNotChangeable();
		}
		if (lz.isMatrix() && lz.getCol() > 0 && lz.getRow() > 0) {
			int capacity = lz.getCol() * lz.getRow();
			zone.setName(lz.getName());
			zone.setColNumber(lz.getCol());
			zone.setRowNumber(lz.getRow());
			zone.setCapacity(capacity);
			zone.setMatrix(true);
		} else if (lz.isMatrix() == false && lz.getCapacity() > 0) {
			zone.setName(lz.getName());
			zone.setColNumber(0);
			zone.setRowNumber(0);
			zone.setCapacity(lz.getCapacity());
			zone.setMatrix(false);
		} else {
			throw new InvalidLocationZone();
		}
		return locationZoneRepository.save(zone);
	}

	@Override
	public List<LocationZone> findAll() {
		return locationZoneRepository.findAll();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void remove(Long id) {
		LocationZone lz = findById(id);
		if (lz == null) {
			throw new LocationZoneNotFound();
		}
		List<Maintenance> activeMaintenances = getActiveMaintenances(id);
		if (activeMaintenances != null && activeMaintenances.isEmpty() == false) {
			throw new LocationZoneNotChangeable();
		}
		locationZoneRepository.deleteById(id);
	}

	@Override
	public ArrayList<Maintenance> getActiveMaintenances(long locationZoneId) {
		return locationZoneRepository.getActiveMaintenances(locationZoneId);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public List<LocationZone> deleteByLocationId(long locationId) {
		return locationZoneRepository.deleteByLocationId(locationId);
	}

}
