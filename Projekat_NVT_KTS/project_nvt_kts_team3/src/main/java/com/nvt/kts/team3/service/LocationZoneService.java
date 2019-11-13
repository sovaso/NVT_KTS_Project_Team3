package com.nvt.kts.team3.service;

import java.util.ArrayList;
import java.util.List;

import com.nvt.kts.team3.dto.LocationZoneDTO;
import com.nvt.kts.team3.model.LocationZone;
import com.nvt.kts.team3.model.Maintenance;

public interface LocationZoneService {
	public LocationZone findById(Long id);
	public LocationZone save(LocationZoneDTO lz);
	public LocationZone update(LocationZoneDTO lz);
	public List<LocationZone> findAll();
	public void remove(Long id);
	public ArrayList<Maintenance> getActiveMaintenances(long locationZoneId);
	public List<LocationZone> deleteByLocationId(long locationId);
}

