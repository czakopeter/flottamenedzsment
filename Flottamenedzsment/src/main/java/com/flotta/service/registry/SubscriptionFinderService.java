package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import com.flotta.model.registry.Subscription;

public interface SubscriptionFinderService {
	
	public List<Subscription> findAll();

  public Optional<Subscription> findById(long id);
  
  public Optional<Subscription> findByNumber(String number);
}
