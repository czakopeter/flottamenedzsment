package com.flotta.repository.registry;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.flotta.model.registry.Device;
import com.flotta.model.registry.DeviceType;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {

  List<Device> findAll();
  
  Device findBySerialNumber(String serialNumber);

  List<Device> findAllByDeviceType(DeviceType deviceType);

}
