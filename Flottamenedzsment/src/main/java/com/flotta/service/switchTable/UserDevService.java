package com.flotta.service.switchTable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.Device;
import com.flotta.model.registry.User;
import com.flotta.model.switchTable.UserDev;
import com.flotta.repository.switchTable.UserDevRepository;

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

//  public List<Device> findAllFreeDeviceByUser(User user) {
//    if(user == null) {
//      return new LinkedList<>();
//    }
//    List<UserDev> udList = userDevRepository.findAllByUser(user);
//    Set<Device> dSet = new HashSet<Device>();
//    udList.forEach(ud -> {
//      if(user.equalsByEmail(userDevRepository.findFirstByDevOrderByBeginDateDesc(ud.getDev()).getUser())) {
//        dSet.add(ud.getDev());
//      }
//    });
//    return new LinkedList<>(dSet);
//  }

  /**
   * @param userOpt
   * @return visszadja az eszközet melyeket a felhasználó birtokol
   */
  public List<Device> findAllCurrentDeviceByUser(Optional<User> userOpt) {
    List<Device> result = new LinkedList<>();
    if(userOpt.isPresent()) {
      for(UserDev userDev : userDevRepository.findAllByUserAndEndDateNull(userOpt.get())) {
        result.add(userDev.getDev());
      }
    }
    return result;
  }
}