package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.flotta.model.registry.Subscription;

@Service
public interface SubscriptionFinderService {
	
	public List<Subscription> findAll();

  public Optional<Subscription> findById(long id);
  
  public Optional<Subscription> findByNumber(String number);
}
