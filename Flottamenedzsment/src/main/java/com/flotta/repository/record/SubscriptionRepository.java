package com.flotta.repository.record;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.record.Subscription;


public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

	List<Subscription> findAll();
	
	Optional<Subscription> findByNumber(String number);
}