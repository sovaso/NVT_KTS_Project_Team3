package com.nvt.kts.team3.service;

import java.time.LocalDateTime;
import java.util.List;

import com.nvt.kts.team3.model.Ticket;

public interface TicketService {
	public Ticket findById(Long id);
	public Ticket save(Ticket ticket);
	public Ticket saveAndFlush(Ticket ticket);
	public List<Ticket> findAll();
	public void remove(Long id);
	public List<Ticket> getMaintenanceReservedTickets(long maintenanceID);
	public List<Ticket> getEventTickets(long eventID);
	public List<Ticket> getEventReservedTickets(long eventID);
	public List<Ticket> getEventSoldTickets(long eventID);
	public List<Ticket> getMaintenanceSoldTickets(long maintenanceID);
	public List<Ticket> getMaintenanceTickets(long maintenanceID);
	public List<Ticket> getExpieredUnpaidTickets(LocalDateTime hourAgo, LocalDateTime now);
	public List<Ticket> getLeasedZoneReservedTickets(long leasedZoneID);
	public List<Ticket> getLeasedZoneSoldTickets(long leasedZoneID);
	public List<Ticket> getLeasedZoneTickets(long leasedZoneID);
	public List<Ticket> deleteByZoneId(long zoneId);
	boolean cancelTicket(Long id);
}

