package com.sign_up.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	/*
	 * @Autowired
	 * 
	 * @Qualifier("mailSender") private JavaMailSender mailSender;
	 * 
	 * public void sendOtp(String to, String otp) { SimpleMailMessage message = new
	 * SimpleMailMessage(); message.setTo(to); message.setSubject("Your OTP Code");
	 * message.setText("Your OTP is: " + otp); mailSender.send(message); }
	 */

}
