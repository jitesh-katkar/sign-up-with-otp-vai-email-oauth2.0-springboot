package com.sign_up.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sign_up.entity.Customer;
import com.sign_up.mapper.CustomerDTOMapper;
import com.sign_up.repository.CustomerRepository;
import com.sign_up.response.CustomerDetailsRepsonseDTO;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	public Customer createCustomer(Customer customer) {
		customer.setPasswordHash(encodePassword(customer.getPasswordHash()));
		return customerRepository.save(customer);
	}

	public Customer getCustomerById(Integer id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
	}

	public List<CustomerDetailsRepsonseDTO> getAllCustomers() {
		System.out.println("Fetching all customers details");

		return CustomerDTOMapper.toDTOList(customerRepository.findAll());
	}

	public void deleteCustomer(Integer id) {
		customerRepository.deleteById(id);
	}

	public boolean verifyPassword(String rawPassword, String hashedPassword) {

		System.out.println("raw pass " + rawPassword);
		System.out.println("hashed pass " + hashedPassword);
		return passwordEncoder.matches(rawPassword, hashedPassword);
	}

	public Optional<Customer> findCustomerByEmailId(String emailId) {
		return Optional.ofNullable(customerRepository.findCustomerByEmailId(emailId).get());
	}

	public ResponseEntity<String> verifyUserCredentials(String emailId, String password) throws UsernameNotFoundException {

			Customer customer = findCustomerByEmailId(emailId).get();
			if (customer != null) {

				if (verifyPassword(password, customer.getPasswordHash())) {
					return ResponseEntity.ok("Login successful...!!! Welcome to application...!!!");
				} else {
					throw new UsernameNotFoundException("Invalid username or password.");
				}
			} else {
				throw new UsernameNotFoundException("User is not registered, please signup first.");
			}
	}

	public ResponseEntity<String> validateUserCredentials(String emailId, String password) {

		Customer customer = findCustomerByEmailId(emailId).get();

		if ("admin".equals(emailId) && verifyPassword(password, customer.getPasswordHash())) {
			return ResponseEntity.ok("Login successful!");
		} else {
			return ResponseEntity.status(401).body("Invalid credentials!");
		}
	}

}
