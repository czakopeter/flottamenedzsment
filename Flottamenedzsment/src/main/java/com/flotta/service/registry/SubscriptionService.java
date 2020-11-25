package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.Subscription;
import com.flotta.model.viewEntity.SubscriptionToView;
import com.flotta.repository.registry.SubscriptionRepository;

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

  public Optional<Subscription> findById(long id) {
    return subscriptionRepository.findById(id);
  }
  
  public Optional<Subscription> findByNumber(String number) {
      return subscriptionRepository.findByNumber(number);
  }
  
  public boolean add(SubscriptionToView stv) {
    Optional<Subscription> optional = subscriptionRepository.findByNumber(stv.getNumber());
    if(optional.isPresent()) {
      appendMsg("Number already exists");
    } else {
      Subscription entity = new Subscription(stv.getNumber(), stv.getBeginDate());
      subscriptionRepository.save(entity);
    }
    return !optional.isPresent();
  }

  public void save(Subscription sub) {
    subscriptionRepository.save(sub);
  }

//  public List<SubscriptionToView> findAllCurrentByUser(User user) {
//    List<SubscriptionToView> result = new LinkedList<SubscriptionToView>();
//    List<Subscription> all = subscriptionRepository.findAll();
//    for(Subscription s : all) {
//      if(Utility.isSameByIdOrBothNull(user, s.getActualUser())) {
//        result.add(s.toView());
//      }
//    }
//    return result;
//  }

}
