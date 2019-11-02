package com.nvt.kts.team3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nvt.kts.team3.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

}
