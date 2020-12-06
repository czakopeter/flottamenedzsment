package com.flotta.repository.registry;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.registry.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	List<Role> findAll();

  Optional<Role> findByRoleIgnoreCase(String string);
}