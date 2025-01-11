package com.sign_up.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sign_up.entity.Customer;
import com.sign_up.request.CustomerLoginRequestBody;
import com.sign_up.response.CustomerDetailsRepsonseDTO;
import com.sign_up.service.CustomerService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PostMapping
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
		return ResponseEntity.ok(customerService.createCustomer(customer));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
		return ResponseEntity.ok(customerService.getCustomerById(id));
	}

	@GetMapping
	public ResponseEntity<List<CustomerDetailsRepsonseDTO>> getAllCustomers() {
		return ResponseEntity.ok(customerService.getAllCustomers());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
		customerService.deleteCustomer(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginToApp(@RequestBody @Valid CustomerLoginRequestBody loginRequestBody) {

		return customerService.verifyUserCredentials(loginRequestBody.getUsernameOrEmail(), 
				loginRequestBody.getPassword());
		
	}
	
	//login with OTP either on email or phone number
}
