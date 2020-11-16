package com.flotta.repository.record;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.record.Sim;

public interface SimRepository extends CrudRepository<Sim, Long> {

	List<Sim> findAll();

  Sim findByImei(String imei);

  List<Sim> findAllBySimSubIsNullAndReasonIsNull();
	
}