package com.example.spring2.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring2.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	boolean existsByEmail(String email);

	boolean existsByMobile(Long mobile);

	Customer findByEmail(String email);

}
