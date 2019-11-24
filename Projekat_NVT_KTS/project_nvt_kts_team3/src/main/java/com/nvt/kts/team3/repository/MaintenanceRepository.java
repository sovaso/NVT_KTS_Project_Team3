package com.nvt.kts.team3.repository;

import java.time.LocalDateTime;
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
			"WHERE m.reservationExpiry >= ?1 AND m.reservationExpiry >= NOW()")
	public List<Maintenance> getExpieredMaintenances(LocalDateTime hourAgo);

	@Query("SELECT m FROM Maintenance m "+
			"WHERE m.reservationExpiry >= ?1 AND  m.reservationExpiry < ?2")
	public List<Maintenance> getWarningMaintenances(LocalDateTime next24hours, LocalDateTime next25hours);
	
	public List<Maintenance> save(List<Maintenance> maintenances);
}

