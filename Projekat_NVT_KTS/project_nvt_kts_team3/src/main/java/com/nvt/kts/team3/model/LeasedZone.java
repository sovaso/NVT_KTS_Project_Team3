package com.nvt.kts.team3.model;

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
@Table(catalog = "dbteam3", name = "leased_zone")
public class LeasedZone {

	@Id
	@GeneratedValue
	private long id;

	// cena sedista unutar zone
	@Column(name = "seat_price")
	private double seatPrice;

	// many to one
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "location_zone_id")
	private LocationZone zone;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "event_location_id")
	private EventLocation eventLocation;

	// one to many
	@JsonIgnore
	@OneToMany(mappedBy = "zone", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<Seat> seats = new HashSet<>();

	public LeasedZone(long id, double seatPrice, LocationZone zone, Set<Seat> seats) {
		super();
		this.id = id;
		this.seatPrice = seatPrice;
		this.zone = zone;
		this.seats = seats;
	}

	public LeasedZone() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getSeatPrice() {
		return seatPrice;
	}

	public void setSeatPrice(double seatPrice) {
		this.seatPrice = seatPrice;
	}

	public LocationZone getZone() {
		return zone;
	}

	public void setZone(LocationZone zone) {
		this.zone = zone;
	}

	public Set<Seat> getSeats() {
		return seats;
	}

	public void setSeats(Set<Seat> seats) {
		this.seats = seats;
	}

}
