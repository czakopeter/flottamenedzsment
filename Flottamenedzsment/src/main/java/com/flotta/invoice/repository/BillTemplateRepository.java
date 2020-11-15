package com.flotta.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.invoice.MyNode;

public interface BillTemplateRepository extends CrudRepository<MyNode, Long> {

  List<MyNode> findAllByParentIsNull();
  
  List<MyNode> findAllByParentIsNullAndName(String name);
}
