package com.nvt.kts.team3.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nvt.kts.team3.model.Maintenance;
import com.nvt.kts.team3.model.LocationZone;

@Repository
public interface LocationZoneRepository extends JpaRepository<LocationZone, Long>{

	@Query("SELECT DISTINCT m FROM Maintenance m " +
		   "WHERE m.maintenanceDate > NOW() " +
		   "AND m.event.status = 1 "+
	   	   "AND m.id IN "+
	   		   "(SELECT lz.maintenance.id from LeasedZone lz "+
	   		   "WHERE zone.id = ?1)")
	public ArrayList<Maintenance> getActiveMaintenances(long locationZoneId);
}
