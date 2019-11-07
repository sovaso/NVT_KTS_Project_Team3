package com.nvt.kts.team3.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nvt.kts.team3.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

	@Query("SELECT DISTINCT t FROM Ticket t " +
			"WHERE t.zone.maintenance.event.id = ?1 "+
			"AND t.reserved = 1")
	public ArrayList<Event> getReservedTickets(Long eventId);
	
	@Query("SELECT DISTINCT t FROM Ticket t " +
			"WHERE t.zone.maintenance.event.id = ?1 "+
			"AND t.reservation.paid = 1")
	public ArrayList<Event> getSoldTickets(Long eventId);
	
	@Query("SELECT e FROM Event e " +
			"WHERE e.status = 1  "+
			"AND e.id IN " +
			"(SELECT DISTINCT m.event.id FROM Maintenance m " +
				"WHERE m.maintenanceDate >= NOW()) ")
	public List<Event> getActiveEvents();
}