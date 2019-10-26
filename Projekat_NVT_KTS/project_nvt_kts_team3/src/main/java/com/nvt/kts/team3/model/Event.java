package com.nvt.kts.team3.model;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(catalog = "dbteam3", name = "event")
public class Event {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "name")
	private String name;

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Column(name = "maintenance")
	private ArrayList<Date> maintenance;

	@Column(name = "status")
	private boolean status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private EventType type;

	@JsonIgnore
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<Ticket> tickets = new HashSet<>();

	@OneToOne(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private EventLocation eventLocation;

	// slike i videi...
	@Column(name = "pictures")
	private ArrayList<String> pictures;
	@Column(name = "videos")
	private ArrayList<String> videos;

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

	public Event(long id, String name, ArrayList<Date> maintenance, boolean status, EventType type,
			EventLocation eventLocation, ArrayList<String> pictures, ArrayList<String> videos) {
		super();
		this.id = id;
		this.name = name;
		this.maintenance = maintenance;
		this.status = status;
		this.type = type;
		this.eventLocation = eventLocation;
		this.pictures = pictures;
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

	public ArrayList<Date> getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(ArrayList<Date> maintenance) {
		this.maintenance = maintenance;
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

	public EventLocation getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(EventLocation eventLocation) {
		this.eventLocation = eventLocation;
	}
}
