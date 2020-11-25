package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.Subscription;
import com.flotta.repository.registry.SubscriptionRepository;

@Service
public class SubscriptionServiceOnlyInfo extends ServiceWithMsg {
	
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired
	public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}

	public List<Subscription> findAll() {
		return subscriptionRepository.findAll();
	}

  public Optional<Subscription> findById(long id) {
    return subscriptionRepository.findById(id);
  }
  
  public Optional<Subscription> findByNumber(String number) {
    return subscriptionRepository.findByNumber(number);
  }
}
