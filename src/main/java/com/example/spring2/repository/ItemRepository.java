package com.example.spring2.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring2.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}