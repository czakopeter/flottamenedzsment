package com.flotta.repository.registry;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.registry.User;

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);
	
	List<User> findAll();

  User findByActivationKey(String key);

  List<User> findAllByStatus(int status);

  List<User> findAllByEnabled(boolean enabled);
	
}