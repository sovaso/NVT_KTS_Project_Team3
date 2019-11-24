package com.nvt.kts.team3.service;

import java.util.List;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;

public interface ReservationService {
	public Reservation findById(Long id);
	public Reservation create(Reservation reservation);
	public List<Reservation> findAll();
	public boolean remove(Long id);
	public List<Reservation> findByUser(RegularUser u);
	public List<Reservation> findByUserAndPaid(RegularUser u,boolean paid);
	public List<Reservation> findByEvent(Event e);
	public boolean payReservation(Long id);
	public List<Reservation> getLocationReservations(Long id);
	public void delete(Long id);
}
