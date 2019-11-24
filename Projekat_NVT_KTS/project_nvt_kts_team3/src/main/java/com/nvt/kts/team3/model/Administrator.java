package com.nvt.kts.team3.model;

import javax.persistence.Entity;

@Entity
public class Administrator extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Administrator() {
		
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
