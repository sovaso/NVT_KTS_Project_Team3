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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(catalog = "dbteam3", name = "location")
public class Location {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "address")
	private String address;

	@Column(name = "description")
	private String description;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_location_id")
	private EventLocation eventLocation;

	// one to many...
	@JsonIgnore
	@OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<LocationZone> locationZones = new HashSet<>();

	public EventLocation getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(EventLocation eventLocation) {
		this.eventLocation = eventLocation;
	}

	public Location(long id, String name, String address, String description, Set<LocationZone> locationZones) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
		this.locationZones = locationZones;
	}

	public Location() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<LocationZone> getLocationZones() {
		return locationZones;
	}

	public void setLocationZones(Set<LocationZone> locationZones) {
		this.locationZones = locationZones;
	}

}
