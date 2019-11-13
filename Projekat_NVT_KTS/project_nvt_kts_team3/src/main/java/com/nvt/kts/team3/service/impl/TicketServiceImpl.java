package com.nvt.kts.team3.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.repository.ReservationRepository;
import com.nvt.kts.team3.repository.TicketRepository;
import com.nvt.kts.team3.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService{

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Override
	public Ticket findById(Long id) {
		return ticketRepository.getOne(id);
	}

	@Override
	public Ticket save(Ticket ticket) {
		return ticketRepository.save(ticket);
	}

	@Override
	public List<Ticket> findAll() {
		return ticketRepository.findAll();
	}

	@Override
	public void remove(Long id) {
		ticketRepository.deleteById(id);
	}

	@Override
	public Ticket saveAndFlush(Ticket ticket) {
		return ticketRepository.saveAndFlush(ticket);
	}

	@Override
	public List<Ticket> getMaintenanceReservedTickets(long maintenanceID) {
		return ticketRepository.getMaintenanceReservedTickets(maintenanceID);
	}
	
	@Override
	public List<Ticket> getMaintenanceSoldTickets(long maintenanceID) {
		return ticketRepository.getMaintenanceSoldTickets(maintenanceID);
	}

	@Override
	public List<Ticket> getMaintenanceTickets(long maintenanceID) {
		return ticketRepository.getMaintenanceTickets(maintenanceID);
	}

	@Override
	public List<Ticket> getEventTickets(long eventID) {
		return ticketRepository.getEventTickets(eventID);
	}

	@Override
	public List<Ticket> getEventReservedTickets(long eventID) {
		return ticketRepository.getEventReservedTickets(eventID);
	}

	@Override
	public List<Ticket> getEventSoldTickets(long eventID) {
		return ticketRepository.getEventSoldTickets(eventID);
	}

	@Override
	public List<Ticket> getLeasedZoneReservedTickets(long leasedZoneID) {
		return ticketRepository.getLeasedZoneReservedTickets(leasedZoneID);
	}

	@Override
	public List<Ticket> getLeasedZoneSoldTickets(long leasedZoneID) {
		return ticketRepository.getLeasedZoneSoldTickets(leasedZoneID);
	}

	@Override
	public List<Ticket> getLeasedZoneTickets(long leasedZoneID) {
		return ticketRepository.getLeasedZoneTickets(leasedZoneID);
	}

	@Override
	public List<Ticket> deleteByZoneId(long zoneId) {
		return ticketRepository.deleteByZoneId(zoneId);
	}

	@Override
	public boolean cancelTicket(Long id) {
		Ticket t = findById(id);
		Reservation r = t.getReservation();
		if (t != null && t.isReserved() == true
				&& t.getZone().getMaintenance().getReservationExpiry().after(new Date())) {
			t.setReserved(false);
			t.setReservation(null);
			r.getReservedTickets().remove(t);
			reservationRepository.save(r);
			save(t);
			if (r.getReservedTickets().size() == 0) {
				reservationRepository.deleteById(r.getId());
				
			}
		} else {
			return false;
		}
		return true;
	}

}
