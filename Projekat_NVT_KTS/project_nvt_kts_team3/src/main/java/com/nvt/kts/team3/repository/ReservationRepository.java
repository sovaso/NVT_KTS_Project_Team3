package com.nvt.kts.team3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	public List<Reservation> findByUser(RegularUser u);
	public List<Reservation> findByEvent(Event e);
	public List<Reservation> findByUserAndPaid(RegularUser u,boolean paid);
	
	@Transactional
	@Modifying
	@Query("DELETE from Reservation r where r.id in ?1")
	public void deleteReservationsWithIds(List<Long> reservations);
}
