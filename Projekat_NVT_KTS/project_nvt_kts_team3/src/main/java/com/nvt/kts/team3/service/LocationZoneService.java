package com.nvt.kts.team3.service;

import java.util.List;

import com.nvt.kts.team3.model.LocationZone;

public interface LocationZoneService {
	public LocationZone findById(Long id);
	public LocationZone save(LocationZone locationZone);
	public List<LocationZone> findAll();
	public void remove(Long id);
}
