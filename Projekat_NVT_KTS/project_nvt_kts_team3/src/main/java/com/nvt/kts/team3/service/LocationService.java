package com.nvt.kts.team3.service;

import java.util.List;

import com.nvt.kts.team3.model. Location;

public interface LocationService {
	public Location findById(Long id);
	public Location save(Location location);
	public List<Location> findAll();
	public void remove(Long id);
}
