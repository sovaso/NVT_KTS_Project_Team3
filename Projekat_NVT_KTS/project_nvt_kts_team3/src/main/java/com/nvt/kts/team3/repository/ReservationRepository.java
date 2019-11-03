package com.nvt.kts.team3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.User;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	public List<Reservation> findByUser(RegularUser u);
	public List<Reservation> findByEvent(Event e);
}
