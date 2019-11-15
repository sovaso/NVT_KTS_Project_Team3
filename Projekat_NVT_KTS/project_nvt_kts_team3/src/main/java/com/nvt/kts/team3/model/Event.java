package com.nvt.kts.team3.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(catalog = "dbteam3", name = "event")
public class Event {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "status")
	private boolean status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private EventType type;

	@Version
	private Long version;

	@JsonIgnore
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<Reservation> reservations = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Maintenance> maintenances = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "location_info_id")
	private Location locationInfo;

	// slike i videi...
	@Column(name = "pictures")
	private ArrayList<String> pictures;
	@Column(name = "videos")
	private ArrayList<String> videos;

	public Event(long id, String name, boolean status, EventType type, Set<Reservation> reservations,
			Set<Maintenance> maintenances, Location locationInfo, ArrayList<String> pictures,
			ArrayList<String> videos) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
		this.type = type;
		this.reservations = reservations;
		this.maintenances = maintenances;
		this.locationInfo = locationInfo;
		this.pictures = pictures;
		this.videos = videos;
	}

	public Event(String name, boolean status, EventType type, Set<Reservation> reservations,
			Set<Maintenance> maintenances, Location locationInfo, ArrayList<String> pictures,
			ArrayList<String> videos) {
		super();
		this.name = name;
		this.status = status;
		this.type = type;
		this.reservations = reservations;
		this.maintenances = maintenances;
		this.locationInfo = locationInfo;
		this.pictures = pictures;
		this.videos = videos;
	}

	public ArrayList<String> getPictures() {
		return pictures;
	}

	public void setPictures(ArrayList<String> pictures) {
		this.pictures = pictures;
	}

	public ArrayList<String> getVideos() {
		return videos;
	}

	public void setVideos(ArrayList<String> videos) {
		this.videos = videos;
	}

	public Event() {
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Set<Maintenance> getMaintenances() {
		return maintenances;
	}

	public void setMaintenances(Set<Maintenance> maintenances) {
		this.maintenances = maintenances;
	}

	public Location getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(Location locationInfo) {
		this.locationInfo = locationInfo;
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
