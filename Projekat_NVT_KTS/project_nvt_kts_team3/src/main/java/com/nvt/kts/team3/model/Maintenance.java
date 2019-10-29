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
@Table(catalog = "dbteam3", name = "event_location")
public class Maintenance {

	@Column(name = "maintenance_date")
	private Date maintenanceDate;

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

	public Maintenance(long id, Set<LeasedZone> leasedZones) {
		super();
		this.id = id;
		this.leasedZones = leasedZones;
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

}
