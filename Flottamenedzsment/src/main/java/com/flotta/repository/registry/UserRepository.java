package com.flotta.repository.registry;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.registry.User;

public interface UserRepository extends CrudRepository<User, Long> {

  List<User> findAll();
  
	Optional<User> findByEmail(String email);

	Optional<User> findByActivationKey(String key);

  List<User> findAllByEnabled(boolean enabled);
	
}