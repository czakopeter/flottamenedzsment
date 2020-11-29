package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.Subscription;
import com.flotta.model.viewEntity.SubscriptionToView;
import com.flotta.repository.registry.SubscriptionRepository;

@Service
public class SubscriptionService extends ServiceWithMsg implements SubscriptionServiceGetter {
	
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired
	public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}
	
	@Override
  public List<Subscription> findAll() {
    return subscriptionRepository.findAll();
  }

	@Override
  public Optional<Subscription> findById(long id) {
    return subscriptionRepository.findById(id);
  }
  
	@Override
  public Optional<Subscription> findByNumber(String number) {
      return subscriptionRepository.findByNumber(number);
  }
  
  public boolean create(SubscriptionToView stv) {
    Optional<Subscription> optional = subscriptionRepository.findByNumber(stv.getNumber());
    if(optional.isPresent()) {
      appendMsg("Number already exists");
    } else {
      Subscription entity = new Subscription(stv.getNumber(), stv.getBeginDate());
      subscriptionRepository.save(entity);
    }
    return !optional.isPresent();
  }

  public void update(Subscription sub) {
    subscriptionRepository.save(sub);
  }
}
