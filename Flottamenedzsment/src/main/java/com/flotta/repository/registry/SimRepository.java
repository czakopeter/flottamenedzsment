package com.flotta.repository.registry;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.enums.SimStatusEnum;
import com.flotta.model.registry.Sim;

public interface SimRepository extends CrudRepository<Sim, Long> {

	List<Sim> findAll();

  Sim findByImei(String imei);

  List<Sim> findAllBySimSubIsNullAndReasonIsNull();

  List<Sim> findAllByStatus(SimStatusEnum free);
}