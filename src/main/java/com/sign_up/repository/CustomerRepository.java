package com.sign_up.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sign_up.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	public Optional<Customer> findCustomerByEmailId(String emailId);

}
