package com.flotta.repository.registry;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.flotta.enums.SimStatusEnum;
import com.flotta.model.registry.Sim;

public interface SimRepository extends CrudRepository<Sim, Long> {

	List<Sim> findAll();

  Optional<Sim> findByImei(String imei);

  List<Sim> findAllBySimSubIsNullAndChangeReasonIsNull();

  List<Sim> findAllByStatus(SimStatusEnum free);
}