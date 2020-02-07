package com.nvt.kts.team3.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "media")
public class Media {
	
	@Id
	@GenericGenerator(name="generator" , strategy="increment")
	@GeneratedValue(generator="generator")
	private long id;
	
	@JoinColumn(name = "event_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	Event event;
	
	@Column(name = "link")
	String link;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Media(long id, Event event, String link) {
		super();
		this.id = id;
		this.event = event;
		this.link = link;
	}

	public Media() {
		super();
	}
	
	
	
	
	
	

}
