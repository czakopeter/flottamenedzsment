package com.flotta.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.invoice.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {

  Category findById(long id);
  
  List<Category> findAll();

  Category findByName(String category);
}
