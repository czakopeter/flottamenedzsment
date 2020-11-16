package com.flotta.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import com.flotta.entity.record.Role;
import com.flotta.repository.record.RoleRepository;

//@SpringBootTest
@DataJpaTest
class RoleRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private RoleRepository roleRepository;
  
  @Test
  void findAdminRole() {
    Role admin = new Role("ADMIN");
    Role basic = new Role("BASIC");
    entityManager.persist(admin);
    entityManager.persist(basic);
    entityManager.flush();
    
    Role found = roleRepository.findByRole("ADMIN");
    assertThat(admin.getRole()).isEqualTo(found.getRole());
  }
  
  @Test
  void findUnknownRole() {
    assertThat(roleRepository.findByRole("UNKNOWN")).isNull();
  }
  
  void findAll() {
    Role admin = new Role("ADMIN");
    Role basic = new Role("BASIC");
    entityManager.persist(admin);
    entityManager.persist(basic);
    entityManager.flush();
    
    List<Role> found = roleRepository.findAll();
    assertTrue(found.size() == 2);
  }

}
