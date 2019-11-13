package com.nvt.kts.team3.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nvt.kts.team3.dto.TicketDTO;

@Entity
@Table(catalog = "dbteam3", name = "ticket")
public class Ticket {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "number_row")
	private int row;

	@Column(name = "number_col")
	private int col;

	@Column(name = "price")
	private double price;

	@Column(name = "reserved")
	private boolean reserved;

	// many to one
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

	// many to one
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name = "leased_zone_id")
	private LeasedZone zone;

	public Ticket() {
		super();
	}

	public Ticket(long id, int row, int col, double price, boolean reserved, Reservation reservation, LeasedZone zone) {
		super();
		this.id = id;
		this.row = row;
		this.col = col;
		this.price = price;
		this.reserved = reserved;
		this.reservation = reservation;
		this.zone = zone;
	}
	
	public Ticket(int row, int col, double price, boolean reserved, Reservation reservation, LeasedZone zone) {
		super();
		this.row = row;
		this.col = col;
		this.price = price;
		this.reserved = reserved;
		this.reservation = reservation;
		this.zone = zone;
	}
	
	public Ticket(TicketDTO ticketDTO) {
		this.id=ticketDTO.getId();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public LeasedZone getZone() {
		return zone;
	}

	public void setZone(LeasedZone zone) {
		this.zone = zone;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
}
