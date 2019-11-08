package com.nvt.kts.team3.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nvt.kts.team3.dto.MessageDTO;
import com.nvt.kts.team3.dto.ReservationDTO;
import com.nvt.kts.team3.dto.TicketDTO;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.ReservationService;
import com.nvt.kts.team3.service.TicketService;
import com.nvt.kts.team3.service.UserService;


import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.RegularUser;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private EventService eventService;

	@Autowired
	private UserService userService;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	

	public ReservationController(ReservationService reservationService, TicketService ticketService,
			EventService eventService, UserService userService) {
		super();
		this.reservationService = reservationService;
		this.ticketService = ticketService;
		this.eventService = eventService;
		this.userService = userService;
	}

	@PostMapping(value = "/createReservation", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<MessageDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
		Reservation r=this.reservationService.create(new Reservation(reservationDTO));
		if(r==null) {
			return new ResponseEntity<>(new MessageDTO("No success", "Reservation could not be done!"), HttpStatus.BAD_REQUEST);
		}
		if(r.getReservedTickets().size()>reservationDTO.getTickets().size()) {
			return new ResponseEntity<>(new MessageDTO("Partly success", "Some tickets could not be reserved, but reservation is created!"), HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity<>(new MessageDTO("Success", "Reservation successfuly created!"),HttpStatus.CREATED);
		}
		
	}

	@DeleteMapping(value = "/deleteReservation/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<MessageDTO> deleteReservation(@PathVariable long reservationId) {
		// dodaj prover da je moguce obrisati rezervaciju samo ako je pre dogadjaja
		Reservation r = this.reservationService.findById(reservationId);
		if (r != null) {
			int noSuccess = 0;
			for (Ticket t : r.getReservedTickets()) {
				if (t.getZone().getMaintenance().getReservationExpiry().after(new Date())
						&& t.getReservation().getId() == r.getId() && t.isReserved() == true) {
					t.setReserved(false);
					t.setReservation(null);
				} else {
					noSuccess++;
				}
			}
			if (noSuccess == 0) {
				this.reservationService.remove(r.getId());
				return new ResponseEntity<>(new MessageDTO("Success", "Reservation successfuly cancelled!"),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new MessageDTO("No success", "Reservation cannot be cancelled!"),
						HttpStatus.BAD_REQUEST);
			}

		} else {
			return new ResponseEntity<>(new MessageDTO("No success", "Reservation cannot be found!"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(value = "/payReservation/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<MessageDTO> payReservation(@PathVariable long reservationId) {

		// dodaj prover da je moguce obrisati rezervaciju samo ako je pre dogadjaja
		Reservation r = this.reservationService.findById(reservationId);
		if (r.getEvent().isStatus() == true) {
			int noSuccess = 0;
			for (Ticket t : r.getReservedTickets()) {
				if (!t.getZone().getMaintenance().getReservationExpiry().after(new Date())) {
					noSuccess++;
				}
			}
			if (noSuccess == 0) {
				r.setPaid(true);
				reservationService.create(r);
				return new ResponseEntity<>(new MessageDTO("Success", "Reservation successfuly paid!"), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new MessageDTO("No success", "Reservation cannot be paid, it expired!"),
						HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(new MessageDTO("No success", "Event is not active!"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/getAllReservations", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Reservation>> getAllReservations() {
		List<Reservation> reservations = this.reservationService.findAll();
		return new ResponseEntity<>(reservations, HttpStatus.OK);
	}

	@GetMapping(value = "/getUserReservations/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<Reservation>> getUserReservations(@PathVariable long userId) {
		List<Reservation> reservations = this.reservationService
				.findByUser((RegularUser) this.userService.findById(userId));
		return new ResponseEntity<>(reservations, HttpStatus.OK);
	}

	@GetMapping(value = "/getReservation/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Reservation> getReservation(@PathVariable long reservationId) {
		Reservation res = this.reservationService.findById(reservationId);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping(value = "/getEventReservations/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Reservation>> getEventReservations(@PathVariable long eventId) {
		List<Reservation> res = this.reservationService.findByEvent(this.eventService.findById(eventId));
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping(value = "/getLocationReservations/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Reservation>> getLocationReservations(@PathVariable long locationId) {
		List<Reservation> res = this.reservationService.findAll();
		List<Reservation> ret = new ArrayList<Reservation>();
		for (Reservation r : res) {
			if (r.getEvent().getLocationInfo().getId() == locationId) {
				ret.add(r);
			}
		}
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@PutMapping(value = "/cancelTicket/{ticketId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<MessageDTO> cancelTicket(@PathVariable long ticketId) {
		// dodaj prover da je moguce obrisati rezervaciju samo ako je pre dogadjaja
		Ticket t = this.ticketService.findById(ticketId);
		Reservation r=t.getReservation();
		if (t != null && t.isReserved() == true
				&& t.getZone().getMaintenance().getReservationExpiry().after(new Date())) {
			t.setReserved(false);
			t.setReservation(null);
			r.getReservedTickets().remove(t);
			reservationService.create(r);
			ticketService.save(t);
			if(r.getReservedTickets().size()==0) {
				reservationService.remove(r.getId());
			}
		} else {
			return new ResponseEntity<>(new MessageDTO("No success", "Ticket cannot be cancelled!"),
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new MessageDTO("Success", "Reservation successfuly cancelled!"), HttpStatus.OK);
	}
	
	
	
	

}
