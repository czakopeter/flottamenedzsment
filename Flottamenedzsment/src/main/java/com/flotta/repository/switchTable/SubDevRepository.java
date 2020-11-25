package com.flotta.repository.switchTable;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.registry.Device;
import com.flotta.model.registry.Subscription;
import com.flotta.model.switchTable.SubDev;

public interface SubDevRepository extends CrudRepository<SubDev, Long> {

  SubDev findFirstBySubOrderByBeginDateDesc(Subscription sub);

  SubDev findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(Subscription sub, LocalDate date);

  SubDev findFirstByDevOrderByBeginDateDesc(Device device);

  SubDev findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(Device dev, LocalDate date);
  
}
