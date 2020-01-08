package com.nvt.kts.team3.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.model.LeasedZone;

@Repository
public interface LeasedZoneRepository extends JpaRepository<LeasedZone, Long>{

	@Query("SELECT DISTINCT lz FROM LeasedZone lz " +
		    "WHERE lz.maintenance.event.id = ?1 ")
	public ArrayList<LeasedZone> getEventLeasedZones(long eventId);
	
	@Transactional
	@Modifying
	public List<LeasedZone> deleteByMaintenanceId(long maintenanceId);
}
