package com.flotta.service.invoice;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.invoice.Category;
import com.flotta.repository.invoice.CategoryRepository;

@Service
public class CategoryService {

  private CategoryRepository categoryRepository;

  @Autowired
  public void setCategoryRepository(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  List<Category> findAll() {
    List<Category> result = categoryRepository.findAll();
    Collections.sort(result);
    return result;
  }
  
  List<Category> findAllByIds(List<Long> ids) {
    return categoryRepository.findAllById(ids);
  }

  Optional<Category> findById(long id) {
    return categoryRepository.findById(id);
  }

  Category createOrModify(long id, String name) {
    Category result;
    
    Optional<Category> optionalByName = categoryRepository.findByName(name);
    Optional<Category> optionalById = categoryRepository.findById(id);
    
    if(optionalById.isPresent()) {
      if(!optionalByName.isPresent()) {
        Category category = optionalById.get();
        category.setName(name);
        result = categoryRepository.save(category);
      } else {
        result = optionalByName.get();
      }
    } else {
      if(optionalByName.isPresent()) {
        result = optionalByName.get();
      } else {
        result = categoryRepository.save(new Category(name));
      }
    }
    return result;
  }
}