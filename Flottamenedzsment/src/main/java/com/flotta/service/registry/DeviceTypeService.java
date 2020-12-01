package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.DeviceType;
import com.flotta.repository.registry.DeviceTypeRepository;

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
  
  public List<DeviceType> findAllVisible() {
    return deviceTypeRepository.findAllByVisibleTrue();
  }

  public boolean create(DeviceType deviceType) {
    if (creatable(deviceType)) {
        deviceTypeRepository.save(deviceType);
        return true;
    }
    return false;
  }
  
  public void update(DeviceType deviceType) {
    if(updatable(deviceType)) {
      deviceTypeRepository.save(deviceType);
    }
  }
  
  private boolean creatable(DeviceType deviceType) {
    Optional<DeviceType> typeByNameOpt = deviceTypeRepository.findByNameIgnoreCase(deviceType.getName());
    Optional<DeviceType> typeByBrandAndModelOpt = deviceTypeRepository.findByBrandAndModelIgnoreCase(deviceType.getBrand(), deviceType.getModel());
    return !(typeByNameOpt.isPresent() || typeByBrandAndModelOpt.isPresent());
  }
  
  private boolean updatable(DeviceType deviceType) {
    Optional<DeviceType> savedTypeOpt = deviceTypeRepository.findById(deviceType.getId());
    Optional<DeviceType> typeByNameOpt = deviceTypeRepository.findByNameIgnoreCase(deviceType.getName());
    Optional<DeviceType> typeByBrandAndModelOpt = deviceTypeRepository.findByBrandAndModelIgnoreCase(deviceType.getBrand(), deviceType.getModel());
      return (typeByNameOpt.isPresent() && typeByNameOpt.get().equals(savedTypeOpt.get())) ||
            (typeByBrandAndModelOpt.isPresent() && typeByBrandAndModelOpt.get().equals(savedTypeOpt.get()));
  }
}
