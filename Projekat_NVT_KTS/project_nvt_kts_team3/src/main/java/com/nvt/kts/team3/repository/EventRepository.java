package com.nvt.kts.team3.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.EventType;
import com.nvt.kts.team3.model.Location;
import com.nvt.kts.team3.model.Ticket;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

	@Query("SELECT DISTINCT t FROM Ticket t " +
			"WHERE t.zone.maintenance.event.id = ?1 "+
			"AND t.reserved = 1")
	public ArrayList<Event> getReservedTickets(Long eventId);
	
	@Query("SELECT DISTINCT t FROM Ticket t " +
			"WHERE t.zone.maintenance.event.id = ?1 "+
			"AND t.reservation.paid = 1")
	public ArrayList<Ticket> getSoldTickets(Long eventId);
	
	@Query("SELECT e FROM Event e " +
			"WHERE e.status = 1  "+
			"AND e.id IN " +
			"(SELECT DISTINCT m.event.id FROM Maintenance m " +
				"WHERE m.maintenanceDate >= NOW()) ")
	public List<Event> getActiveEvents();
	
	public List<Event> findByName(String name);
	public List<Event> findByLocationInfo(Location locationInfo);
	public List<Event> findByType(EventType type);
	
	
	@Query(
	"SELECT DISTINCT e FROM Event e left join Maintenance m on e.id = m.event.id"
	+ " left join Location l on e.locationInfo.id = l.id"
	+ " where  e.name = ?1 or l.address = ?1 or e.type = ?1")
	public ArrayList<Event> searchEventOnlyField(String field);
	
	@Query(
			"SELECT DISTINCT e FROM Event e left join Maintenance m on e.id = m.event.id"
			+ " left join Location l on e.locationInfo.id = l.id"
			+ " where m.maintenanceDate = ?1")
			public ArrayList<Event> searchEventSpecDate(LocalDateTime startDate);
	
	@Query(
			"SELECT DISTINCT e FROM Event e left join Maintenance m on e.id = m.event.id"
			+ " left join Location l on e.locationInfo.id = l.id"
			+ " where (e.name = ?1 or l.address = ?1 or e.type = ?1) and m.maintenanceDate = ?2")
	public ArrayList<Event> searchEventFieldSpecDate(String field, LocalDateTime startDate);

	
	@Query(
			"SELECT DISTINCT e FROM Event e left join Maintenance m on e.id = m.event.id"
			+ " left join Location l on e.locationInfo.id = l.id"
			+ " where m.maintenanceDate between ?1 and ?2")
	public ArrayList<Event> searchEventPeriod(LocalDateTime startDate, LocalDateTime endDate);
	
	@Query(
			"SELECT DISTINCT e FROM Event e left join Maintenance m on e.id = m.event.id"
			+ " left join Location l on e.locationInfo.id = l.id"
			+ " where ((e.name = ?1) or (l.address = ?1) or (e.type = ?1)) and (m.maintenanceDate between ?2 and ?3)")
	public ArrayList<Event> searchEventFieldPeriod(String field, LocalDateTime startDate, LocalDateTime endDate);
	
	
	
	@Query("SELECT e FROM Event e left join Maintenance m on e.id = m.event.id"
			+ " order by m.maintenanceDate")
	public ArrayList<Event> findAllSortedDateAcs();
	
	@Query("SELECT e FROM Event e left join Maintenance m on e.id = m.event.id"
			+ " order by m.maintenanceDate desc")
	public ArrayList<Event> findAllSortedDateDesc();
}