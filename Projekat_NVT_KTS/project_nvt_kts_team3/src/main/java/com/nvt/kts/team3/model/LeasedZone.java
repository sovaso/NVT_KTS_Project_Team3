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
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(catalog = "dbteam3", name = "leased_zone")
public class LeasedZone {

	@Id
	@GenericGenerator(name="generator" , strategy="increment")
	@GeneratedValue(generator="generator")
	private long id;

	// cena sedista unutar zone
	@Column(name = "seat_price")
	private double seatPrice;

	@Version
	private Long version;

	// many to one
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "location_zone_id")
	private LocationZone zone;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "maintenance_id")
	private Maintenance maintenance;

	// one to many
	@JsonIgnore
	@OneToMany(mappedBy = "zone", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Ticket> tickets = new HashSet<>();

	public LeasedZone() {
		super();
	}

	public LeasedZone(long id, double seatPrice, LocationZone zone, Maintenance maintenance, Set<Ticket> tickets) {
		super();
		this.id = id;
		this.seatPrice = seatPrice;
		this.zone = zone;
		this.maintenance = maintenance;
		this.tickets = tickets;
	}

	public LeasedZone(double seatPrice, LocationZone zone, Maintenance maintenance, Set<Ticket> tickets) {
		super();
		this.seatPrice = seatPrice;
		this.zone = zone;
		this.maintenance = maintenance;
		this.tickets = tickets;
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

	public Maintenance getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(Maintenance maintenance) {
		this.maintenance = maintenance;
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
