package com.flotta.repository.record;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.record.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByEmail(String email);
	
	List<User> findAll();

  User findByActivationKey(String key);

  List<User> findAllByStatus(int status);

  List<User> findAllByEnabled(boolean enabled);
	
}