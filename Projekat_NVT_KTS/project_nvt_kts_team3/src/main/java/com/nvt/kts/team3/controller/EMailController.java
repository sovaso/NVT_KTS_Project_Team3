package com.nvt.kts.team3.controller;

import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nvt.kts.team3.dto.Mail;
import com.nvt.kts.team3.security.TokenHelper;



@RestController
public class EMailController {

	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;

	@Autowired
	TokenHelper tokenUtils;

	@PostMapping("/sendEmail")
	public void sendEmail(@RequestBody Mail mail) throws AddressException, MessagingException {

		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");

		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getEmailAddress()));
		generateMailMessage.setSubject(mail.getSubject());
		String emailBody = mail.getBody();
		generateMailMessage.setContent(emailBody, "text/html");

		Transport transport = getMailSession.getTransport("smtp");
		transport.connect("smtp.gmail.com", "nvtktsteam3@gmail.com", "nvtktsteam3!maki");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
	
	public void sendEmails(Set<String> emailAdresses, String subject, String body) throws AddressException, MessagingException{
		for(String email : emailAdresses){
			Mail mail = new Mail(email, subject, body);
			sendEmail(mail);
		}
	}
	
}
