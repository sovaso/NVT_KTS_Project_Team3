package com.nvt.kts.team3.service;

import java.util.ArrayList;
import java.util.List;

import com.nvt.kts.team3.dto.LeasedZoneDTO;
import com.nvt.kts.team3.model.LeasedZone;

public interface LeasedZoneService {
	public LeasedZone findById(Long id);
	public LeasedZone save(LeasedZoneDTO leasedZoneDTO);
	public LeasedZone update(LeasedZoneDTO leasedZoneDTO);
	public List<LeasedZone> findAll();
	public void remove(Long id);
	public ArrayList<LeasedZone> getEventLeasedZones(long eventId);
	public List<LeasedZone> deleteByMaintenanceId(long maintenanceId);
}