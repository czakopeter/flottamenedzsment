package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.registry.Sim;
import com.flotta.model.registry.Subscription;
import com.flotta.model.viewEntity.SubscriptionToView;
import com.flotta.repository.registry.SubscriptionRepository;
import com.flotta.utility.BooleanWithMessages;
import com.flotta.utility.Validator;

@Service
public class SubscriptionService implements SubscriptionFinderService {
	
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
  
  public BooleanWithMessages create(SubscriptionToView stv, Optional<Sim> simOpt) {
    BooleanWithMessages eb = new BooleanWithMessages(Validator.validHunPhoneNumber(stv.getNumber()));
    if(eb.booleanValue()) {
      Optional<Subscription> optional = subscriptionRepository.findByNumber(stv.getNumber());
      if(optional.isPresent()) {
        eb.setFalse();
        eb.addMessage(MessageKey.PHONE_NUMBER_ALREADY_USED, MessageType.WARNING);
      } else {
        Subscription entity = new Subscription(stv.getNumber(), stv.getBeginDate());
        entity.addSim(simOpt, null, stv.getBeginDate());
        subscriptionRepository.save(entity);
      }
    } else {
      eb.addMessage(MessageKey.PHONE_NUMBER_INVALID, MessageType.WARNING);
    }
    return eb;
  }

  public void update(Subscription sub) {
    subscriptionRepository.save(sub);
  }
}
