package com.flotta.repository.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.registry.Subscription;


public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

	List<Subscription> findAll();
	
	Optional<Subscription> findByNumber(String number);
}