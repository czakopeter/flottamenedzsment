package com.flotta.repository.invoice;


import org.springframework.data.repository.CrudRepository;

import com.flotta.model.invoice.FeeItem;

public interface FeeItemRepository extends CrudRepository<FeeItem, Long> {

}
