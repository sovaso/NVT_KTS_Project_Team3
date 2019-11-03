package com.nvt.kts.team3.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService{

	@Autowired
	private ReservationRepository reservationRepository;
	
	@Override
	public Reservation findById(Long id) {
		return reservationRepository.getOne(id);
	}

	@Override
	public Reservation save(Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	@Override
	public List<Reservation> findAll() {
		return reservationRepository.findAll();
	}

	@Override
	public void remove(Long id) {
		reservationRepository.deleteById(id);
	}

	@Override
	public List<Reservation> findByUser(RegularUser u) {
		return reservationRepository.findByUser(u);
	}

	@Override
	public List<Reservation> findByEvent(Event e) {
		return reservationRepository.findByEvent(e);
	}

}
