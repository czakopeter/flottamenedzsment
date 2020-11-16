package com.flotta.repository.switchTable;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.record.Subscription;
import com.flotta.entity.switchTable.SubSim;

public interface SubSimRepository extends CrudRepository<SubSim, Long> {

  List<SubSim> findAllBySub(Subscription s);

  SubSim findBySubAndBeginDate(Subscription s, LocalDate date);

  void deleteBySubAndBeginDate(Subscription s, LocalDate date);

  SubSim findBySubAndBeginDate(long subId, LocalDate date);

  List<SubSim> findAllBySub(long subId);

  SubSim findFirstBySubOrderByBeginDateDesc(Subscription sub);
}
