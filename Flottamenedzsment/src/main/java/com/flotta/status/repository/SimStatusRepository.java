package com.flotta.status.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.record.Sim;
import com.flotta.status.SimStatus;

public interface SimStatusRepository extends CrudRepository<SimStatus, Long> {

  List<SimStatus> findAllBySim(Sim sim);

  SimStatus findFirstBySimOrderByDateDesc(Sim sim);

}
