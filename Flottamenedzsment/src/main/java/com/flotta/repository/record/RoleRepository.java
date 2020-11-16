package com.flotta.repository.record;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.record.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByRole(String role);
	
	List<Role> findAll();
}