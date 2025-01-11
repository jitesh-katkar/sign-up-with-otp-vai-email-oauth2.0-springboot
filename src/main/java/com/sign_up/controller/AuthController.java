package com.sign_up.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sign_up.service.AuthService;
import com.sign_up.util.GMailerService;

@RestController
@RequestMapping("/customers/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private GMailerService gMailerService;
	
	 @PostMapping("/send-otp")
	    public ResponseEntity<String> sendOtp(@RequestParam String email) throws Exception {
	        //authService.generateAndSendOtp(email);
	        
	        gMailerService.sendMail(email);
	        
	        return ResponseEntity.ok("OTP sent to " + email);
	    }

	    @PostMapping("/verify-otp")
	    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
	        boolean isValid = authService.validateOtp(email, otp);
	        if (isValid) {
	            return ResponseEntity.ok("Login successful");
	        }
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
	    }

}
