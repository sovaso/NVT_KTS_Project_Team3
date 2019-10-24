package com.nvt.kts.team3.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(catalog = "dbteam3", name = "seat")
public class Seat {

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

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private ZoneType seatType;

	// many to one
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ticket_id")
	private Ticket reservation;

	// many to one
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "leased_zone_id")
	private LeasedZone zone;

	public Seat(long id, int row, int col, double price, boolean reserved, ZoneType seatType, Ticket reservation,
			LeasedZone zone) {
		super();
		this.id = id;
		this.row = row;
		this.col = col;
		this.price = price;
		this.reserved = reserved;
		this.seatType = seatType;
		this.reservation = reservation;
		this.zone = zone;
	}

	public Seat() {
		super();
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

	public ZoneType getSeatType() {
		return seatType;
	}

	public void setSeatType(ZoneType seatType) {
		this.seatType = seatType;
	}

	public Ticket getReservation() {
		return reservation;
	}

	public void setReservation(Ticket reservation) {
		this.reservation = reservation;
	}

	public LeasedZone getZone() {
		return zone;
	}

	public void setZone(LeasedZone zone) {
		this.zone = zone;
	}
}
