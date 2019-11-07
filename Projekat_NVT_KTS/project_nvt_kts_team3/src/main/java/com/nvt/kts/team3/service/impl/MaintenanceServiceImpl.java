package com.nvt.kts.team3.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.repository.MaintenanceRepository;
import com.nvt.kts.team3.service.MaintenanceService;

@Service
public class MaintenanceServiceImpl implements MaintenanceService{

	@Autowired
	private MaintenanceRepository maintenanceRepository;
	
	@Override
	public Maintenance findById(Long id) {
		return maintenanceRepository.getOne(id);
	}

	@Override
	@Transactional
	public Maintenance save(Maintenance maintenance) {
		return maintenanceRepository.save(maintenance);
	}

	@Override
	public List<Maintenance> findAll() {
		return maintenanceRepository.findAll();
	}

	@Override
	public void remove(Long id) {
		maintenanceRepository.deleteById(id);
	}

	@Override
	@Transactional
	public Maintenance saveAndFlush(Maintenance maintenance) {
		return maintenanceRepository.saveAndFlush(maintenance);
	}

	@Override
	public Maintenance getLastMaintenanceOfEvent(long eventId) {
		return maintenanceRepository.getLastMaintenanceOfEvent(eventId);
	}

}
