package com.flotta.status.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.registry.Device;
import com.flotta.status.DeviceStatus;

public interface DeviceStatusRepository extends CrudRepository<DeviceStatus, Long> {

  List<DeviceStatus> findAllByDev(Device dev);

  DeviceStatus findFirstByDevOrderByDateDesc(Device dev);

  long countByDev(Device dev);

  DeviceStatus findFirstByDevAndDateBeforeOrderByDateDesc(Device dev, LocalDate date);

}
