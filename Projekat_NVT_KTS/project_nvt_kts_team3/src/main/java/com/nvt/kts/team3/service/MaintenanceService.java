package com.nvt.kts.team3.service;

import java.text.ParseException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.nvt.kts.team3.dto.MaintenanceDTO;
import com.nvt.kts.team3.model.Maintenance;

public interface MaintenanceService {
	public Maintenance findById(Long id);
	public Maintenance save(MaintenanceDTO maintenanceDTO) throws ParseException;
	public Maintenance updateMaintenance(MaintenanceDTO maintenanceDTO) throws ParseException;
	public List<Maintenance> findAll();
	public void remove(long id);
	public Maintenance getLastMaintenanceOfEvent(long eventId);
	public List<Maintenance> removeByEventId(long eventId);
	public List<Maintenance> save(List<Maintenance> maintenances);
	public void checkForExpieredTickets() throws AddressException, MessagingException;
	public void warnUsersAboutExpiry() throws AddressException, MessagingException;
}