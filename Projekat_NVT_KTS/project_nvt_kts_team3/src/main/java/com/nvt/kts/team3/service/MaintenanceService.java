package com.nvt.kts.team3.service;

import java.util.List;

import com.nvt.kts.team3.model.Maintenance;

public interface MaintenanceService {
	public Maintenance findById(Long id);
	public Maintenance save(Maintenance maintenance);
	public Maintenance saveAndFlush(Maintenance maintenance);
	public List<Maintenance> findAll();
	public void remove(Long id);
	public Maintenance getLastMaintenanceOfEvent(long eventId);
}