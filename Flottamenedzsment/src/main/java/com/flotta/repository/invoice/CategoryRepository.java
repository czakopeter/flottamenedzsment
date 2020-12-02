package com.flotta.repository.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.invoice.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

  List<Category> findAll();
  
  Optional<Category> findByName(String name);
}
