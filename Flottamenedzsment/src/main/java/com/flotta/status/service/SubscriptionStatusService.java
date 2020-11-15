package com.flotta.status.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.entity.record.Subscription;
import com.flotta.enums.SubscriptionStatusEnum;
import com.flotta.status.SubscriptionStatus;
import com.flotta.status.repository.SubscriptionStatusRepository;

@Service
public class SubscriptionStatusService {

  private SubscriptionStatusRepository subscriptionStatusRepository;

  @Autowired
  public void setSubscriptionStatusRepository(SubscriptionStatusRepository subscriptionStatusRepository) {
    this.subscriptionStatusRepository = subscriptionStatusRepository;
  }

  public SubscriptionStatus findById(long id) {
    return subscriptionStatusRepository.findById(id).orElse(null);
  }
  
  public List<SubscriptionStatus> findAllBySub(Subscription sub) {
    return subscriptionStatusRepository.findAllBySub(sub);
  }

  public boolean save(Subscription sub, SubscriptionStatusEnum status, LocalDate date) {
    subscriptionStatusRepository.save(new SubscriptionStatus(status, sub, date));
    return true;
  }
  
  public void deleteLastStatus(Subscription sub) {
    long pcs = subscriptionStatusRepository.countBySub(sub);
    SubscriptionStatus status = subscriptionStatusRepository.findFirstBySubOrderByDateDesc(sub);
    if(pcs == 1) {
      status.setStatus(SubscriptionStatusEnum.FREE);
      subscriptionStatusRepository.save(status);
    } else if(pcs > 1) {
      subscriptionStatusRepository.deleteById(status.getId());
    }
  }

  public void setStatus(Subscription sub, SubscriptionStatusEnum status, LocalDate date) {
    long pcs = subscriptionStatusRepository.countBySub(sub);
    
    if(pcs == 0) {
      subscriptionStatusRepository.save(new SubscriptionStatus(status, sub, date));
    } else {
      SubscriptionStatus last = subscriptionStatusRepository.findFirstBySubOrderByDateDesc(sub);
      if(date.isAfter(last.getDate())) {
        //add new status
        if(!status.equals(last.getStatus())) {
          subscriptionStatusRepository.save(new SubscriptionStatus(status, sub, date));
        }
      } else if (date.isEqual(last.getDate())) {
        //modify last
        SubscriptionStatus lastBefore = subscriptionStatusRepository.findFirstBySubAndDateBeforeOrderByDateDesc(sub, date);
        if(lastBefore != null && lastBefore.getStatus().equals(status)) {
          subscriptionStatusRepository.deleteById(last.getId());
        } else {
          last.setStatus(status);
          subscriptionStatusRepository.save(last);
        }
      } else {
        //error
      }
    }
  }
}
