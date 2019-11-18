package com.nvt.kts.team3.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.nvt.kts.team3.model.Event;
import com.nvt.kts.team3.model.RegularUser;
import com.nvt.kts.team3.model.Reservation;
import com.nvt.kts.team3.model.User;
import com.nvt.kts.team3.service.EventService;
import com.nvt.kts.team3.service.ReservationService;
import com.nvt.kts.team3.service.TicketService;
import com.nvt.kts.team3.service.UserService;

import exception.EventNotFound;
import exception.ReservationNotFound;

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
		Reservation r = this.reservationService.create(new Reservation(reservationDTO));
		if (r.getReservedTickets().size() < reservationDTO.getTickets().size()) {
			return new ResponseEntity<>(
					new MessageDTO("Partly success", "Some tickets could not be reserved, but reservation is created!"),
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(new MessageDTO("Success", "Reservation successfuly created!"),
					HttpStatus.CREATED);
		}

	}

	@DeleteMapping(value = "/deleteReservation/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<MessageDTO> deleteReservation(@PathVariable long reservationId) {
		boolean message = this.reservationService.remove(reservationId);

		return new ResponseEntity<>(new MessageDTO("Success", "Reservation successfuly cancelled!"), HttpStatus.OK);

	}

	@PutMapping(value = "/payReservation/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<MessageDTO> payReservation(@PathVariable long reservationId) {
		boolean value = this.reservationService.payReservation(reservationId);
		return new ResponseEntity<>(new MessageDTO("Success", "Reservation successfuly paid!"), HttpStatus.OK);

	}

	@GetMapping(value = "/getAllReservations", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Reservation>> getAllReservations() {
		List<Reservation> reservations = this.reservationService.findAll();
		return new ResponseEntity<>(reservations, HttpStatus.OK);
	}

	@GetMapping(value = "/getUserReservations/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getUserReservations(@PathVariable long userId) {
		User u = this.userService.findById(userId);
		if (u != null) {
			List<Reservation> reservations = this.reservationService
					.findByUser((RegularUser) this.userService.findById(userId));
			return new ResponseEntity<>(reservations, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new MessageDTO("No success", "User with given id does not exist!"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/getReservation/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<?> getReservation(@PathVariable long reservationId) {
		Reservation res = this.reservationService.findById(reservationId);
		if (res != null) {
			return new ResponseEntity<>(res, HttpStatus.OK);
		} else {
			throw new ReservationNotFound();
		}
	}

	@GetMapping(value = "/getEventReservations/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Reservation>> getEventReservations(@PathVariable long eventId) {
		Event e = this.eventService.findById(eventId);
		if (e != null) {
			List<Reservation> res = this.reservationService.findByEvent(this.eventService.findById(eventId));
			return new ResponseEntity<>(res, HttpStatus.OK);
		} else {
			throw new EventNotFound();
		}
	}

	@GetMapping(value = "/getLocationReservations/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getLocationReservations(@PathVariable long locationId) {
		List<Reservation> ret = this.reservationService.getLocationReservations(locationId);
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}

	@PutMapping(value = "/cancelTicket/{ticketId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<MessageDTO> cancelTicket(@PathVariable long ticketId) {
		// dodaj prover da je moguce obrisati rezervaciju samo ako je pre dogadjaja
		Boolean b = this.ticketService.cancelTicket(ticketId);
		return new ResponseEntity<>(new MessageDTO("Success", "Reservation successfuly cancelled!"), HttpStatus.OK);
	}

}
