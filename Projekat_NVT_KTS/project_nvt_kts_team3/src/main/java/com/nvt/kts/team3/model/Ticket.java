package com.nvt.kts.team3.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(catalog = "dbteam3", name = "ticket")
public class Ticket {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "dates")
	private ArrayList<Date> dates;

	@Column(name = "date_of_reservation")
	private Date dateOfReservation;

	@Column(name = "expiry_date")
	private Date expiryDate;

	@Column(name = "paid")
	private boolean paid;

	@Column(name = "ticket_price")
	private double ticketPrice;

	// many to one
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private RegularUser user;

	// one to many
	@JsonIgnore
	@OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<Seat> reservedSeats = new HashSet<>();

	// many to one
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "event_id")
	private Event event;

	// QR kod...
	@Column(name = "qr_code")
	private String qrCode;

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public Ticket() {
		super();
	}

	public Ticket(long id, ArrayList<Date> dates, Date dateOfReservation, Date expiryDate, boolean paid,
			double ticketPrice, RegularUser user, Set<Seat> reservedSeats, Event event, String qrCode) {
		super();
		this.id = id;
		this.dates = dates;
		this.dateOfReservation = dateOfReservation;
		this.expiryDate = expiryDate;
		this.paid = paid;
		this.ticketPrice = ticketPrice;
		this.user = user;
		this.reservedSeats = reservedSeats;
		this.event = event;
		this.qrCode = qrCode;
	}

	public ArrayList<Date> getDates() {
		return dates;
	}

	public void setDates(ArrayList<Date> dates) {
		this.dates = dates;
	}

	public Date getDateOfReservation() {
		return dateOfReservation;
	}

	public void setDateOfReservation(Date dateOfReservation) {
		this.dateOfReservation = dateOfReservation;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public RegularUser getUser() {
		return user;
	}

	public void setUser(RegularUser user) {
		this.user = user;
	}

	public Set<Seat> getReservedSeats() {
		return reservedSeats;
	}

	public void setReservedSeats(Set<Seat> reservedSeats) {
		this.reservedSeats = reservedSeats;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
