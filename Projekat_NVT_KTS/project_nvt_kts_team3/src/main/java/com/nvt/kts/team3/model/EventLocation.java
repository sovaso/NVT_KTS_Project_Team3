package com.nvt.kts.team3.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(catalog = "dbteam3", name = "event_location")
public class EventLocation {

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "location_info_id")
	private Location locationInfo;

	// one to many
	@JsonIgnore
	@OneToMany(mappedBy = "eventLocation", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<LeasedZone> leasedZones = new HashSet<>();

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	public EventLocation(long id, Location locationInfo, Set<LeasedZone> leasedZones) {
		super();
		this.id = id;
		this.locationInfo = locationInfo;
		this.leasedZones = leasedZones;
	}

	public EventLocation() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Location getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(Location locationInfo) {
		this.locationInfo = locationInfo;
	}

	public Set<LeasedZone> getLeasedZones() {
		return leasedZones;
	}

	public void setLeasedZones(Set<LeasedZone> leasedZones) {
		this.leasedZones = leasedZones;
	}

}
