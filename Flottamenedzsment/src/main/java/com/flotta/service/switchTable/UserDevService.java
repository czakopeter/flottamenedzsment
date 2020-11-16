package com.flotta.service.switchTable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.entity.record.Device;
import com.flotta.entity.record.User;
import com.flotta.entity.switchTable.UserDev;
import com.flotta.entity.viewEntity.DeviceToView;
import com.flotta.repository.switchTable.UserDevRepository;

@Service
public class UserDevService {

  private UserDevRepository userDevRepository;
  
  @Autowired
  public UserDevService(UserDevRepository userDevRepository) {
    this.userDevRepository = userDevRepository;
  }

  public List<Device> findAllFreeDeviceByUser(User user) {
    if(user == null) {
      return new LinkedList<>();
    }
    List<UserDev> udList = userDevRepository.findAllByUser(user);
    Set<Device> dSet = new HashSet<Device>();
    udList.forEach(ud -> {
      if(user.equalsByEmail(userDevRepository.findFirstByDevOrderByBeginDateDesc(ud.getDev()).getUser())) {
        dSet.add(ud.getDev());
      }
    });
    return new LinkedList<>(dSet);
  }

  public User findLastUser(Device device) {
    return userDevRepository.findFirstByDevOrderByBeginDateDesc(device).getUser();
  }

  public List<DeviceToView> findAllDeviceByUser(User user) {
    List<DeviceToView> result = new LinkedList<>();
    List<UserDev> userDevs = userDevRepository.findAllByUserOrderByBeginDateDesc(user);
    for(UserDev userDev : userDevs) {
      if(userDev.getDev() != null) {
        DeviceToView actual = userDev.getDev().toView(userDev.getBeginDate());
        actual.setEndDate(userDev.getEndDate());
        result.add(actual);
      }
    }
    return result;
  }
}