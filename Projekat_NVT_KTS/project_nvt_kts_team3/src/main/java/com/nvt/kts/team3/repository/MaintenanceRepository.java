package com.nvt.kts.team3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nvt.kts.team3.model.Maintenance;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long>{

	@Query("SELECT m FROM Maintenance m " +
			"WHERE m.maintenanceDate = (SELECT MAX(m1.maintenanceDate) FROM Maintenance m1 WHERE m1.event.id = ?1) AND m.event.id = ?1")
	public Maintenance getLastMaintenanceOfEvent(long eventId);
}

