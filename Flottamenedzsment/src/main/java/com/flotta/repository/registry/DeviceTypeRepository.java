package com.flotta.repository.registry;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.flotta.enums.Availability;
import com.flotta.model.registry.DeviceType;

public interface DeviceTypeRepository extends CrudRepository<DeviceType, Long> {

	List<DeviceType> findAll();

	@Query("select distinct d.brand from DeviceType d")
  List<String> findAllBrandOfDevicesType();

  Optional<DeviceType> findByNameIgnoreCase(String name);

  Optional<DeviceType> findByBrandAndModelIgnoreCase(String brand, String model);

  List<DeviceType> findAllByAvailability(Availability availability);

}