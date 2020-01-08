package com.nvt.kts.team3.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.model.Maintenance;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long>{

	@Query("SELECT m FROM Maintenance m " +
			"WHERE m.maintenanceDate = (SELECT MAX(m1.maintenanceDate) FROM Maintenance m1 WHERE m1.event.id = ?1) AND m.event.id = ?1")
	public Maintenance getLastMaintenanceOfEvent(long eventId);
	
	@Transactional
	@Modifying
	public List<Maintenance> deleteByEventId(long eventId);

	@Query("SELECT m FROM Maintenance m "+
			"WHERE m.reservationExpiry >= ?1 AND  m.reservationExpiry < ?2")
	public List<Maintenance> getWarningMaintenances(LocalDateTime next24hours, LocalDateTime next25hours);
	

	@Query("SELECT DISTINCT m FROM Maintenance m " +
			"WHERE m.event.locationInfo.id = ?1 "+
			"AND m.event.status = 1 "+
			"AND ((m.maintenanceDate <= ?3 AND m.maintenanceEndTime >= ?3) " +
			"		OR (m.maintenanceDate <= ?2 AND m.maintenanceEndTime >= ?3) " +
			"		OR (m.maintenanceDate >= ?2 AND m.maintenanceEndTime <= ?3) " +
			"		OR (m.maintenanceDate >= ?2 AND m.maintenanceDate <= ?3) " +
			"		OR (m.maintenanceEndTime >= ?2 AND m.maintenanceEndTime <= ?3))")
	public ArrayList<Maintenance> getMaintenancesForDate(Long locationId, LocalDateTime startDate, LocalDateTime endDate);
}

