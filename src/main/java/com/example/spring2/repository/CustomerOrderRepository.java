package com.example.spring2.repository;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring2.entity.Customer;
import com.example.spring2.entity.CustomerOrder;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, String> {

	List<CustomerOrder> findByCustomer(Customer customer);

}