package com.nvt.kts.team3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nvt.kts.team3.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{
	
	@Query("SELECT t FROM Ticket t " +
		    "WHERE t.zone.maintenance.event.id = ?1")
	public List<Ticket> getEventTickets(long eventID);
	
	@Query("SELECT t FROM Ticket t " +
		    "WHERE t.zone.maintenance.event.id = ?1 AND t.reserved = 1")
	public List<Ticket> getEventReservedTickets(long eventID);
	
	@Query("SELECT t FROM Ticket t " +
		    "WHERE t.zone.maintenance.event.id = ?1 AND t.reservation.paid = 1")
	public List<Ticket> getEventSoldTickets(long eventID);

	@Query("SELECT t FROM Ticket t " +
		    "WHERE t.zone.maintenance.id = ?1 AND t.reserved = 1")
	public List<Ticket> getMaintenanceReservedTickets(long maintenanceID);
	
	@Query("SELECT t FROM Ticket t " +
		    "WHERE t.zone.maintenance.id = ?1 AND t.reservation.paid = 1")
	public List<Ticket> getMaintenanceSoldTickets(long maintenanceID);
	
	@Query("SELECT t FROM Ticket t " +
		    "WHERE t.zone.maintenance.id = ?1")
	public List<Ticket> getMaintenanceTickets(long maintenanceID);
	
	@Query("SELECT t FROM Ticket t " +
		    "WHERE t.zone.id = ?1 AND t.reserved = 1")
	public List<Ticket> getLeasedZoneReservedTickets(long leasedZoneID);
	
	@Query("SELECT t FROM Ticket t " +
		    "WHERE t.zone.id = ?1 AND t.reservation.paid = 1")
	public List<Ticket> getLeasedZoneSoldTickets(long leasedZoneID);
	
	@Query("SELECT t FROM Ticket t " +
		    "WHERE t.zone.id = ?1")
	public List<Ticket> getLeasedZoneTickets(long leasedZoneID);
}
