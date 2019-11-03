package com.nvt.kts.team3.dto;

public class MaintenanceDTO {
	private String startDate;
	private String endDate;
	private long id;
	
	public MaintenanceDTO(String startDate, String endDate, long id) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.id = id;
	}

	public MaintenanceDTO() {
		super();
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}

