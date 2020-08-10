package com.flotta.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.DeviceType;

public interface DeviceTypeRepository extends CrudRepository<DeviceType, Long> {

	List<DeviceType> findAll();

	@Query("select distinct d.brand from DeviceType d")
  List<String> findAllBrandOfDevicesType();

  DeviceType findByNameIgnoreCase(String name);

  DeviceType findByBrandAndModelIgnoreCase(String brand, String model);

}