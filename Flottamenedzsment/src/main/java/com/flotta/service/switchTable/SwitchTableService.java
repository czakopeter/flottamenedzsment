package com.flotta.service.switchTable;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.User;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.model.viewEntity.SubscriptionToView;

@Service
public class SwitchTableService {
  
  private UserSubService userSubService;
  
  private UserDevService userDevService;

  @Autowired
  public SwitchTableService(UserSubService userSubService, UserDevService userDevService) {
    this.userSubService = userSubService;
    this.userDevService = userDevService;
  }
  
  public List<DeviceToView> findAllDevicesByUser(User user) {
    List<DeviceToView> result = new LinkedList<>();
    userDevService.findAllFreeDeviceByUser(user).forEach(d -> {
      result.add(d.toView());
    });
    return result;
  }
  
//TODO visszamenőlegesen kiírtnál a kezdeti dátumnál levő egyéb infomációkat írja ki, lehet volt változás az időszak alatt amíg a felhasználónál volt
  public List<SubscriptionToView> findAllCurrentSubscriptionByUser(User user) {
    return userSubService.findAllSubscriptionByUser(user);
//    return subscriptionService.findAllCurrentByUser(getCurrentUser());
  }

  public List<DeviceToView> findAllDeviceByUser(User user) {
    return userDevService.findAllDeviceByUser(user);
  }
  
  public List<DeviceToView> findAllCurrentDeviceByUser(User user) {
    return userDevService.findAllCurrentDeviceByUser(user);
  }
  
}
