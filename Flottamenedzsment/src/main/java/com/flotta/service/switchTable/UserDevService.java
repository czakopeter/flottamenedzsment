package com.flotta.service.switchTable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.Device;
import com.flotta.model.registry.User;
import com.flotta.model.switchTable.UserDev;
import com.flotta.repository.switchTable.UserDevRepository;
import com.flotta.utility.Utility;

/**
 * @author CzP
 *
 */
@Service
public class UserDevService {

  private UserDevRepository userDevRepository;
  
  @Autowired
  public UserDevService(UserDevRepository userDevRepository) {
    this.userDevRepository = userDevRepository;
  }

  /**
   * @param userOpt
   * @return visszadja az eszközet melyeket a felhasználó birtokol
   */
  public List<Device> findAllAvailableDeviceByUser(Optional<User> userOpt) {
    List<Device> result = new LinkedList<>();
    if(userOpt.isPresent()) {
      for(UserDev userDev : userDevRepository.findAllByUserAndEndDateNull(userOpt.get())) {
        result.add(userDev.getDev());
      }
    }
    return result;
  }
}