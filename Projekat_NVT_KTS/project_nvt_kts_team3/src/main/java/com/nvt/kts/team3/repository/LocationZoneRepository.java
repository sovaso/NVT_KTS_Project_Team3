package com.nvt.kts.team3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nvt.kts.team3.model.LocationZone;

@Repository
public interface LocationZoneRepository extends JpaRepository<LocationZone, Long>{

}
