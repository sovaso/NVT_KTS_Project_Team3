package com.nvt.kts.team3.service;

import java.util.ArrayList;
import java.util.List;

import com.nvt.kts.team3.model.LeasedZone;

public interface LeasedZoneService {
	public LeasedZone findById(Long id);
	public LeasedZone save(LeasedZone leasedZone);
	public LeasedZone saveAndFlush(LeasedZone leasedZone);
	public List<LeasedZone> findAll();
	public void remove(Long id);
	public ArrayList<LeasedZone> getEventLeasedZones(long eventId);
}