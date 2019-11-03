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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(catalog = "dbteam3", name = "maintenance")
public class Maintenance {

	@Column(name = "maintenance_date")
	private Date maintenanceDate;
	
	@Column(name = "maintenance_end_time")
	private Date maintenanceEndTime;
	
	@Column(name = "reservation_expiry")
	private Date reservationExpiry;

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Id
	@GeneratedValue
	private long id;

	// one to many
	@JsonIgnore
	@OneToMany(mappedBy = "maintenance", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<LeasedZone> leasedZones = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "event_id")
	private Event event;

	public Maintenance(Date maintenanceDate, Date maintenanceEndTime, Date reservationExpiry, long id,
			Set<LeasedZone> leasedZones, Event event) {
		super();
		this.maintenanceDate = maintenanceDate;
		this.maintenanceEndTime = maintenanceEndTime;
		this.reservationExpiry = reservationExpiry;
		this.id = id;
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

	public Date getMaintenanceDate() {
		return maintenanceDate;
	}

	public void setMaintenanceDate(Date maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}

	public Date getReservationExpiry() {
		return reservationExpiry;
	}

	public void setReservationExpiry(Date reservationExpiry) {
		this.reservationExpiry = reservationExpiry;
	}

	public Date getMaintenanceEndTime() {
		return maintenanceEndTime;
	}

	public void setMaintenanceEndTime(Date maintenanceEndTime) {
		this.maintenanceEndTime = maintenanceEndTime;
	}
}
