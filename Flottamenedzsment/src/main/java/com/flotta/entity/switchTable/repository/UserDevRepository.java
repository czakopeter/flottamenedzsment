package com.flotta.entity.switchTable.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.Device;
import com.flotta.entity.User;
import com.flotta.entity.switchTable.UserDev;

public interface UserDevRepository extends CrudRepository<UserDev, Long>{

  UserDev findFirstByDevOrderByBeginDateDesc(Device dev);

  UserDev findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(Device dev, LocalDate date);

  List<UserDev> findAllByUser(User user);
  
  List<UserDev> findAllByDev(Device dev);

}
