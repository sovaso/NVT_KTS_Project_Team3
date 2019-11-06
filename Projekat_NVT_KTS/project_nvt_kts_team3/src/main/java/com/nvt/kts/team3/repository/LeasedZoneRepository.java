package com.nvt.kts.team3.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nvt.kts.team3.model.LeasedZone;

@Repository
public interface LeasedZoneRepository extends JpaRepository<LeasedZone, Long>{

	@Query("SELECT DISTINCT lz FROM LeasedZone lz " +
		    "WHERE lz.maintenance.event = ?1 ")
	public ArrayList<LeasedZone> getEventLeasedZones(long eventId);
}
