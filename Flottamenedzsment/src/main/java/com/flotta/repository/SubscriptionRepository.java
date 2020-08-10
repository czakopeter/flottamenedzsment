package com.flotta.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.Subscription;


public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

	List<Subscription> findAll();
	
	Subscription findByNumber(String number);
}