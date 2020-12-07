package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.enums.Availability;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.registry.DeviceType;
import com.flotta.repository.registry.DeviceTypeRepository;
import com.flotta.utility.ExtendedBoolean;

@Service
public class DeviceTypeService {

  private DeviceTypeRepository deviceTypeRepository;

  @Autowired
  public void setDeviceTypeRepository(DeviceTypeRepository deviceTypeRepository) {
    this.deviceTypeRepository = deviceTypeRepository;
  }

  public List<DeviceType> findAll() {
    return deviceTypeRepository.findAll();
  }

  public Optional<DeviceType> findById(long id) {
    return deviceTypeRepository.findById(id);
  }

  public List<String> findAllBrandOfDeviceTypes() {
    return deviceTypeRepository.findAllBrandOfDevicesType();
  }
  
  public Optional<DeviceType> findByName(String name) {
    return deviceTypeRepository.findByNameIgnoreCase(name);
  }
  
  public List<DeviceType> findAllAvailability(Availability availability) {
    return deviceTypeRepository.findAllByAvailability(availability);
  }

  public ExtendedBoolean create(DeviceType deviceType) {
    ExtendedBoolean eb = creatable(deviceType);
    if (eb.isValid()) {
        deviceTypeRepository.save(deviceType);
        eb.addMessage(MessageKey.SUCCESSFULL_CREATION, MessageType.SUCCESS);
    }
    return eb;
  }
  
  public void update(DeviceType deviceType) {
    if(updatable(deviceType)) {
      deviceTypeRepository.save(deviceType);
    }
  }
  
  private ExtendedBoolean creatable(DeviceType deviceType) {
    Optional<DeviceType> typeByNameOpt = deviceTypeRepository.findByNameIgnoreCase(deviceType.getName());
    Optional<DeviceType> typeByBrandAndModelOpt = deviceTypeRepository.findByBrandAndModelIgnoreCase(deviceType.getBrand(), deviceType.getModel());
    ExtendedBoolean eb = new ExtendedBoolean(!typeByNameOpt.isPresent() || typeByBrandAndModelOpt.isPresent());
    if(typeByNameOpt.isPresent()) {
      eb.addMessage(MessageKey.NAME_ALREADY_EXISTS, MessageType.WARNING);
    }
    if(typeByBrandAndModelOpt.isPresent()) {
      eb.addMessage(MessageKey.BRAND_AND_MODEL_ALREADY_EXISTS, MessageType.WARNING);
    }
    return eb;
  }
  
  private boolean updatable(DeviceType deviceType) {
    return true;
  }
}
