package com.nvt.kts.team3.dto;

public class TicketDTO {
	private long id;
	private String qrCode;
	private long eventId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public TicketDTO(long id, String qrCode, long eventId) {
		super();
		this.id = id;
		this.qrCode = qrCode;
		this.eventId = eventId;
	}
	public TicketDTO() {
		super();
	}
	
	

}
