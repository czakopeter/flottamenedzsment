package com.flotta.repository.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.flotta.model.registry.Device;
import com.flotta.model.registry.DeviceType;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {

  List<Device> findAll();
  
  Optional<Device> findBySerialNumber(String serialNumber);

  List<Device> findAllByDeviceType(DeviceType deviceType);

}
