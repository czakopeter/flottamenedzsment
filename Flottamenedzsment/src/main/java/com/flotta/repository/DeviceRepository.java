package com.flotta.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.record.Device;
import com.flotta.entity.record.DeviceType;

public interface DeviceRepository extends CrudRepository<Device, Long> {

  List<Device> findAll();
  
  Device findBySerialNumber(String serialNumber);

  List<Device> findAllByDeviceType(DeviceType deviceType);

}
