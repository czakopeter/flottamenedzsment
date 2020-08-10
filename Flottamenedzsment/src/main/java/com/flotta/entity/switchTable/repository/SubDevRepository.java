package com.flotta.entity.switchTable.repository;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.Device;
import com.flotta.entity.Subscription;
import com.flotta.entity.switchTable.SubDev;

public interface SubDevRepository extends CrudRepository<SubDev, Long> {

  SubDev findFirstBySubOrderByBeginDateDesc(Subscription sub);

  SubDev findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(Subscription sub, LocalDate date);

  SubDev findFirstByDevOrderByBeginDateDesc(Device device);

  SubDev findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(Device dev, LocalDate date);
  
}
