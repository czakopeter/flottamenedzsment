package com.flotta.service.switchTable;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.User;
import com.flotta.model.switchTable.UserSub;
import com.flotta.model.viewEntity.SubscriptionToView;
import com.flotta.repository.switchTable.UserSubRepository;

@Service
public class UserSubService {

  private UserSubRepository userSubRepository;
  
  
  @Autowired
  public void setUserSubRepository(UserSubRepository userSubRepository) {
    this.userSubRepository = userSubRepository;
  }

//  public List<SubscriptionToView> findAllSubscriptionByUser(User user) {
//    List<SubscriptionToView> result = new LinkedList<>();
//    List<UserSub> userSubs = userSubRepository.findAllByUserOrderByBeginDateDesc(user);
//    for(UserSub userSub : userSubs) {
//      if(userSub.getSub() != null) {
//        SubscriptionToView actual = userSub.getSub().toView(userSub.getBeginDate());
//        actual.setEndDate(userSub.getEndDate());
//        result.add(actual);
//      }
//    }
//    return result;
//  }
}
