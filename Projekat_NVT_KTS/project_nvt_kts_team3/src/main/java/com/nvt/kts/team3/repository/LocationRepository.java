package com.nvt.kts.team3.repository;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>{
	
	public Location findByNameAndAddress(String name, String address);
	
	@Query("SELECT DISTINCT e FROM Event e " +
		    "WHERE e.id IN " +
		    "(SELECT DISTINCT m.event.id FROM Maintenance m " +
				"WHERE m.event.locationInfo.id = ?1 "+
		   		"AND m.maintenanceDate > NOW())")
	public ArrayList<Event> getActiveEvents(long locationId);
	
	@Query("SELECT DISTINCT l FROM Location l WHERE l.status = 1")
	public ArrayList<Location> getActiveLocations();
	
	@Query("SELECT DISTINCT e FROM Event e " +
		    "WHERE e.id IN " +
		    "(SELECT DISTINCT m.event.id FROM Maintenance m " +
				"WHERE m.event.locationInfo.id = ?1 "+
		   		"AND ((m.maintenanceDate <= ?2 AND m.maintenanceEndTime >= ?3 ) OR (m.maintenanceDate >= ?2 AND m.maintenanceEndTime <= ?3 ) OR (m.maintenanceDate >= ?2 AND m.maintenanceDate <= ?3 ) OR (m.maintenanceEndTime >= ?2 AND m.maintenanceEndTime <= ?3 )))")
	public ArrayList<Event> checkIfAvailable(Long locationId, Date startDate, Date endDate);
}

