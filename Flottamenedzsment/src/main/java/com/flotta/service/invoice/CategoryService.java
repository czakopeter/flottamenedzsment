package com.flotta.service.invoice;

import java.util.Collections;
import java.util.LinkedList;
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
    List<Category> result = new LinkedList<>();
    ids.forEach(id -> {
      categoryRepository.findById(id).ifPresent(category -> result.add(category));
    });
    return result;
  }

  Optional<Category> findById(long id) {
    return categoryRepository.findById(id);
  }

  Category createOrModify(long id, String name) {
    Optional<Category> optionalByName = categoryRepository.findByName(name);

    if (optionalByName.isPresent()) {
      return optionalByName.get();
    }

    Optional<Category> optionalById = categoryRepository.findById(id);
    Category category;
    if (optionalById.isPresent()) {
      category = optionalById.get();
      category.setName(name);
    } else {
      category = categoryRepository.save(new Category(name));
    }
    return categoryRepository.save(category);
  }
}