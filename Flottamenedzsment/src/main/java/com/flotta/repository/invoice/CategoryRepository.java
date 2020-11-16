package com.flotta.repository.invoice;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.invoice.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

  Category findById(long id);
  
  List<Category> findAll();

  Category findByName(String category);
}
