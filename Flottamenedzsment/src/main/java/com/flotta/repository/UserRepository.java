package com.flotta.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByEmail(String email);
	
	List<User> findAll();

  User findByPasswordRenewerKey(String key);

  List<User> findAllByStatus(int status);

  List<User> findAllByEnabled(boolean enabled);
	
}