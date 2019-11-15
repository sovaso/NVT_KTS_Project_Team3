package com.nvt.kts.team3.dto;

import java.sql.Timestamp;
import java.util.List;

import com.nvt.kts.team3.model.Authority;

public class UserDTO {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String surname;

	private String username;

	private String email;

	private String password;
	
	private Timestamp lastPasswordResetDate;
	
	private boolean enabled;
	
	private List<Authority> authorities;
	
	public UserDTO() {
		
	}
	

	public UserDTO(Long id, String name, String surname, String username, String email, String password,
			Timestamp lastPasswordResetDate, boolean enabled, List<Authority> authorities) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.lastPasswordResetDate = lastPasswordResetDate;
		this.enabled = enabled;
		this.authorities = authorities;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
