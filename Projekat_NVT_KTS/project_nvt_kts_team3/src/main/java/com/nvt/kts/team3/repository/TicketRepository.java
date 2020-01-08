package com.nvt.kts.team3.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
		    "WHERE (t.zone.maintenance.id = ?1 AND t.reserved = 1)")
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
	
	@Query("SELECT t FROM Ticket t WHERE t.reserved = 1 AND t.reservation.paid = 0 AND t.zone.maintenance.id IN "+
			"(SELECT m.id FROM Maintenance m "+
			"WHERE m.reservationExpiry >= ?1 AND m.reservationExpiry <= ?2)")
	public List<Ticket> getExpieredUnpaidTickets(LocalDateTime hourAgo, LocalDateTime now);
	
	
	
	@Transactional
	@Modifying
	public List<Ticket> deleteByZoneId(long zoneId);
}
