package com.flotta.service.switchTable;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.Device;
import com.flotta.model.registry.User;

@Service
public class SwitchTableService {
  
  
  private UserDevService userDevService;

  @Autowired
  public SwitchTableService(UserDevService userDevService) {
    this.userDevService = userDevService;
  }
  
  public List<Device> findAllCurrentDeviceByUser(Optional<User> userOpt) {
    return userDevService.findAllCurrentDeviceByUser(userOpt);
  }
  
}
