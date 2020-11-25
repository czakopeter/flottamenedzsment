package com.flotta.repository.invoice;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.invoice.MyNode;

public interface InvoiceTemplateRepository extends CrudRepository<MyNode, Long> {

  List<MyNode> findAllByParentIsNull();
  
  List<MyNode> findAllByParentIsNullAndName(String name);
}
