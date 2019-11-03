package com.nvt.kts.team3.dto;

import java.util.ArrayList;

public class ReservationDTO {
	private long eventId;
	private ArrayList<TicketDTO> tickets;
	private String qrCode;
	private long userId;
	
	public ReservationDTO(long eventId, ArrayList<TicketDTO> tickets, String qrCode, long userId) {
		super();
		this.eventId = eventId;
		this.tickets = tickets;
		this.qrCode = qrCode;
		this.userId = userId;
	}
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public ArrayList<TicketDTO> getTickets() {
		return tickets;
	}
	public void setTickets(ArrayList<TicketDTO> tickets) {
		this.tickets = tickets;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public ReservationDTO() {
		super();
	}
	
	

}
