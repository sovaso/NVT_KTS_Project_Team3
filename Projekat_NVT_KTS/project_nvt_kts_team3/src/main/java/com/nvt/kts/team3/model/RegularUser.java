package com.nvt.kts.team3.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class RegularUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<Reservation> reservations = new HashSet<>();

	public RegularUser() {
		
	}
	public RegularUser(long id, String name, String surname, String username, String email, String password) {
		super(id, name, surname, username, email, password);
	}

	public RegularUser(long id, String name, String surname, String username, String email, String password,
			Set<Reservation> reservations) {
		super(id, name, surname, username, email, password);
		this.reservations = reservations;
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
