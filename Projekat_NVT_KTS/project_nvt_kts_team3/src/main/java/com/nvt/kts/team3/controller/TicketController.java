package com.nvt.kts.team3.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nvt.kts.team3.dto.QRCodeDTO;
import com.nvt.kts.team3.model.Ticket;
import com.nvt.kts.team3.service.TicketService;

import exception.TicketNotFound;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="http://localhost:4200", allowedHeaders = "*")
public class TicketController {
	
	@Autowired
	private TicketService ticketService;

	@GetMapping(value = "/getQRCodeImage/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[]  getQRCodeImage(@PathVariable(value = "id") Long ticketId) throws WriterException, IOException {
		// TODO Proveriti da li karta ima vlasnika
		// TODO Provera da li je vlasnik karte poslao zahtev
		Ticket ticket = ticketService.findById(ticketId);
		if(ticket == null){
			throw new TicketNotFound();
		}

	    String code = String.valueOf(ticket.getId());
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, 350, 350);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", bao);
        return bao.toByteArray();
//        QRCodeDTO dto=new QRCodeDTO();
//        dto.setQrBytes(bao.toByteArray());
//        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
	
	@GetMapping(value = "/getEventTickets/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<List<Ticket>> getEventTickets(@PathVariable long eventId) {
		List<Ticket> tickets=ticketService.getEventTickets(eventId);
		return new ResponseEntity<>(tickets, HttpStatus.OK);
	}

	@GetMapping(value = "/getEventReservedTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<List<Ticket>> getEventReservedTickets(@PathVariable(value = "id") Long eventId) {
		return new ResponseEntity<>(ticketService.getEventReservedTickets(eventId), HttpStatus.OK);
	}

	@GetMapping(value = "/getEventSoldTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<List<Ticket>> getEventSoldTickets(@PathVariable(value = "id") Long eventId) {
		return new ResponseEntity<>(ticketService.getEventSoldTickets(eventId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMaintenanceTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<List<Ticket>> getMaintenanceTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getMaintenanceTickets(maintenanceId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMaintenanceReservedTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<List<Ticket>> getMaintenanceReservedTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getMaintenanceReservedTickets(maintenanceId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getMaintenanceSoldTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<List<Ticket>> getMaintenanceSoldTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getMaintenanceSoldTickets(maintenanceId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getLeasedZoneSoldTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<List<Ticket>> getLeasedZoneSoldTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getLeasedZoneSoldTickets(maintenanceId), HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/getLeasedZoneReservedTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Ticket>> getLeasedZoneReservedTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getLeasedZoneReservedTickets(maintenanceId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getLeasedZoneTickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getLeasedZoneTickets(@PathVariable(value = "id") Long maintenanceId){
		return new ResponseEntity<>(ticketService.getLeasedZoneTickets(maintenanceId), HttpStatus.OK);
	}
	
	@DeleteMapping(value="/deleteByZoneId/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Ticket>> deleteByZoneId(@PathVariable(value = "id") Long zoneId){
		return new ResponseEntity<>(ticketService.deleteByZoneId(zoneId), HttpStatus.OK);
	}
	
	
	@GetMapping(value="/getExpiredUnpaidTickets",produces=MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Ticket>> getExpiredUnpaidTickets(){
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime hourAgo=currentTime.minusHours(1L);
		return new ResponseEntity<>(ticketService.getExpieredUnpaidTickets(hourAgo, currentTime),HttpStatus.OK);
	}
}
