package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.Subscription;
import com.flotta.repository.registry.SubscriptionRepository;

@Service
public interface SubscriptionServiceGetter {
	
	public List<Subscription> findAll();

  public Optional<Subscription> findById(long id);
  
  public Optional<Subscription> findByNumber(String number);
}
