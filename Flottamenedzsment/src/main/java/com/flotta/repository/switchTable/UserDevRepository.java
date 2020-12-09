package com.flotta.repository.switchTable;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.registry.User;
import com.flotta.model.switchTable.UserDev;

public interface UserDevRepository extends CrudRepository<UserDev, Long>{

  List<UserDev> findAllByUserAndEndDateNull(User user);

}
