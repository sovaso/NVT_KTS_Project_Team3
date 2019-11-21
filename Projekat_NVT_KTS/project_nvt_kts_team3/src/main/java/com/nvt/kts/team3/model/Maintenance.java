package com.nvt.kts.team3.model;

import java.time.LocalDate;
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

@Entity
@Table(catalog = "dbteam3", name = "maintenance")
public class Maintenance {

	@Column(name = "maintenance_date")
	private LocalDate maintenanceDate;

	@Column(name = "maintenance_end_time")
	private LocalDate maintenanceEndTime;

	@Column(name = "reservation_expiry")
	private LocalDate reservationExpiry;

	@Version
	private Long version;

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Id
	@GenericGenerator(name="generator" , strategy="increment")
	@GeneratedValue(generator="generator")
	private long id;

	// one to many
	@JsonIgnore
	@OneToMany(mappedBy = "maintenance", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<LeasedZone> leasedZones = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "event_id")
	private Event event;

	public Maintenance(LocalDate maintenanceDate, LocalDate maintenanceEndTime, LocalDate reservationExpiry, long id,
			Set<LeasedZone> leasedZones, Event event) {
		super();
		this.maintenanceDate = maintenanceDate;
		this.maintenanceEndTime = maintenanceEndTime;
		this.reservationExpiry = reservationExpiry;
		this.id = id;
		this.leasedZones = leasedZones;
		this.event = event;
	}

	public Maintenance(LocalDate maintenanceDate, LocalDate maintenanceEndTime, LocalDate reservationExpiry,
			Set<LeasedZone> leasedZones, Event event) {
		super();
		this.maintenanceDate = maintenanceDate;
		this.maintenanceEndTime = maintenanceEndTime;
		this.reservationExpiry = reservationExpiry;
		this.leasedZones = leasedZones;
		this.event = event;
	}

	public Maintenance() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<LeasedZone> getLeasedZones() {
		return leasedZones;
	}

	public void setLeasedZones(Set<LeasedZone> leasedZones) {
		this.leasedZones = leasedZones;
	}

	public LocalDate getMaintenanceDate() {
		return maintenanceDate;
	}

	public void setMaintenanceDate(LocalDate maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}

	public LocalDate getReservationExpiry() {
		return reservationExpiry;
	}

	public void setReservationExpiry(LocalDate reservationExpiry) {
		this.reservationExpiry = reservationExpiry;
	}

	public LocalDate getMaintenanceEndTime() {
		return maintenanceEndTime;
	}

	public void setMaintenanceEndTime(LocalDate maintenanceEndTime) {
		this.maintenanceEndTime = maintenanceEndTime;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
