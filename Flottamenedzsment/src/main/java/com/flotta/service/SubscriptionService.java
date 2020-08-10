package com.flotta.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.entity.Subscription;
import com.flotta.entity.User;
import com.flotta.entity.viewEntity.SubscriptionToView;
import com.flotta.repository.SubscriptionRepository;
import com.flotta.utility.Utility;

@Service
public class SubscriptionService extends ServiceWithMsg {
	
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired
	public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}
	
	public List<Subscription> findAll() {
		return subscriptionRepository.findAll();
	}

  public Subscription findById(long id) {
    return subscriptionRepository.findById(id).orElse(null);
  }
  
  public Subscription findByNumber(String number) {
    if(number != null) {
      return subscriptionRepository.findByNumber(number);
    }
    return null;
  }
  
  public boolean add(SubscriptionToView stv) {
    if(subscriptionRepository.findByNumber(stv.getNumber()) == null) {
      Subscription entity = new Subscription(stv.getNumber(), stv.getDate());
      subscriptionRepository.save(entity);
      return true;
    } else {
      appendMsg("Number already exists");
      return false;
    }
  }

  public void save(Subscription sub) {
    subscriptionRepository.save(sub);
  }

  public List<SubscriptionToView> findAllCurrentByUser(User user) {
    List<SubscriptionToView> result = new LinkedList<SubscriptionToView>();
    List<Subscription> all = subscriptionRepository.findAll();
    for(Subscription s : all) {
      if(Utility.isSameByIdOrBothNull(user, s.getActualUser())) {
        result.add(s.toView());
      }
    }
    return result;
  }

}
