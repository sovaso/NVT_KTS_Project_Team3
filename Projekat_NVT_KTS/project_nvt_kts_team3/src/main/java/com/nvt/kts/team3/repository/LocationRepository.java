package com.nvt.kts.team3.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>{
	
	public Location findByNameAndAddress(String name, String address);
	
	@Query("SELECT DISTINCT e FROM Event e " +
		    "WHERE e.status = 1 AND e.id IN " +
		    "(SELECT DISTINCT m.event.id FROM Maintenance m " +
				"WHERE m.event.locationInfo.id = ?1 "+
		   		"AND m.maintenanceDate > NOW())")
	public ArrayList<Event> getActiveEvents(long locationId);
	
	@Query("SELECT DISTINCT l FROM Location l WHERE l.status = 1")
	public ArrayList<Location> getActiveLocations();
	
	@Query("SELECT DISTINCT m.event FROM Maintenance m " +
				"WHERE m.event.locationInfo.id = ?1 "+
				"AND m.event.status = 1 "+
				"AND ((m.maintenanceDate <= ?3 AND m.maintenanceEndTime >= ?3) " +
				"		OR (m.maintenanceDate <= ?2 AND m.maintenanceEndTime >= ?3) " +
				"		OR (m.maintenanceDate >= ?2 AND m.maintenanceEndTime <= ?3) " +
				"		OR (m.maintenanceDate >= ?2 AND m.maintenanceEndTime <= ?3))")
	public ArrayList<Event> checkIfAvailable(Long locationId, LocalDateTime startDate, LocalDateTime endDate);
	
	public Location findByAddress(String address);
}

