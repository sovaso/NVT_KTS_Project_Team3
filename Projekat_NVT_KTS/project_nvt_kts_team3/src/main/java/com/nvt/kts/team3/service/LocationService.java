package com.nvt.kts.team3.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nvt.kts.team3.dto.LocationDTO;
import com.nvt.kts.team3.dto.LocationReportDTO;
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model. Location;

public interface LocationService {
	public Location findById(Long id);
	public Location save(LocationDTO locationDTO);
	public Location update(LocationDTO locationDTO);
	public List<Location> findAll();
	public ArrayList<Location> findAllActive();
	public void remove(Long id);
	public Location findByNameAndAddress(String name, String address);
	public ArrayList<Event> getActiveEvents(long locationId);
	public ArrayList<Event> checkIfAvailable(Long locationId, Date startDate, Date endDate);
	public LocationReportDTO getLocationReport(Long id);
}
