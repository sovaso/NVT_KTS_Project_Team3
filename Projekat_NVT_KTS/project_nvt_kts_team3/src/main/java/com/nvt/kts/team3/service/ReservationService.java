package com.nvt.kts.team3.service;

import java.util.List;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.User;

public interface ReservationService {
	public Reservation findById(Long id);
	public Reservation save(Reservation reservation);
	public List<Reservation> findAll();
	public void remove(Long id);
	public List<Reservation> findByUser(RegularUser u);
	public List<Reservation> findByEvent(Event e);
}
