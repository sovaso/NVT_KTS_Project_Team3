package com.nvt.kts.team3.model;

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
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nvt.kts.team3.dto.ReservationDTO;
import com.nvt.kts.team3.dto.TicketDTO;

@Entity
@Table(name = "reservation")
public class Reservation {

	@Id
	@GenericGenerator(name="generator" , strategy="increment")
	@GeneratedValue(generator="generator")
	private long id;

	@Column(name = "date_of_reservation")
	private Date dateOfReservation;

	@Column(name = "paid")
	private boolean paid;

	@Column(name = "total_price")
	private double totalPrice;

	@Version
	private Long version;

	// many to one
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "user_id")
	private RegularUser user;

	// one to many
	@JsonIgnore
	@OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Ticket> reservedTickets = new HashSet<>();

	// many to one
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "event_id")
	private Event event;

	public Reservation() {
		super();
	}

	public Reservation(long id, Date dateOfReservation, boolean paid, double totalPrice, RegularUser user,
			Set<Ticket> reservedTickets, Event event, String qrCode) {
		super();
		this.id = id;
		this.dateOfReservation = dateOfReservation;
		this.paid = paid;
		this.totalPrice = totalPrice;
		this.user = user;
		this.reservedTickets = reservedTickets;
		this.event = event;
	}

	public Reservation(ReservationDTO reservationDTO) {
		this.dateOfReservation = new Date();
		this.paid = false;
		this.totalPrice = 0;
		this.reservedTickets = new HashSet<>();
		for (TicketDTO ticket : reservationDTO.getTickets()) {
			this.reservedTickets.add(new Ticket(ticket));
		}
		this.event = reservationDTO.getEvent();
	}

	public Date getDateOfReservation() {
		return dateOfReservation;
	}

	public void setDateOfReservation(Date dateOfReservation) {
		this.dateOfReservation = dateOfReservation;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public RegularUser getUser() {
		return user;
	}

	public void setUser(RegularUser user) {
		this.user = user;
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

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Set<Ticket> getReservedTickets() {
		return reservedTickets;
	}

	public void setReservedTickets(Set<Ticket> reservedTickets) {
		this.reservedTickets = reservedTickets;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
