package com.sign_up.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sign_up.entity.Customer;
import com.sign_up.repository.CustomerRepository;
import com.sign_up.util.OtpUtil;

@Service
public class AuthService {

	@Autowired
    private EmailService emailService;
	
	@Autowired
	private CustomerRepository customerRepository;

    public void generateAndSendOtp(String email) {
    	System.out.println("email to sent otp "+email);
        String otp = OtpUtil.generateOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        Customer customer = customerRepository.findCustomerByEmailId(email).orElseGet(() -> {
            Customer newUser = new Customer();
            newUser.setEmailId(email);
            return newUser;
        });

        customer.setOtp(otp);
        customer.setOtpExpiry(expiry);
        customerRepository.save(customer);

        //emailService.sendOtp(email, otp);
    }

    public boolean validateOtp(String email, String otp) {
    	Customer user = customerRepository.findCustomerByEmailId(email).orElseThrow(() -> 
            new RuntimeException("User not found"));

        if (user.getOtp().equals(otp) && LocalDateTime.now().isBefore(user.getOtpExpiry())) {
            user.setOtp(null);
            user.setOtpExpiry(null);
            customerRepository.save(user);
            return true;
        }

        return false;
    }
}
