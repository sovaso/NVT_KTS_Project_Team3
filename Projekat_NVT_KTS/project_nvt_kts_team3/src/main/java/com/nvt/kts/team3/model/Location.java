package com.nvt.kts.team3.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

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

	@Column(name = "status")
	private boolean status;

	@Version
	private Long version;

	@JsonIgnore
	@OneToMany(mappedBy = "locationInfo", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<Event> events = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<LocationZone> locationZones = new HashSet<>();

	public Location(long id, String name, String address, String description, boolean status, Set<Event> events,
			Set<LocationZone> locationZones) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.description = description;
		this.status = status;
		this.events = events;
		this.locationZones = locationZones;
	}

	public Location(String name, String address, String description, boolean status, Set<Event> events,
			Set<LocationZone> locationZones) {
		super();
		this.name = name;
		this.address = address;
		this.description = description;
		this.status = status;
		this.events = events;
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

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
