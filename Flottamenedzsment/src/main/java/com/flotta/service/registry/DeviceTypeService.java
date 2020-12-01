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

  public DeviceType findById(long id) {
    return deviceTypeRepository.findById(id).orElse(null);
  }

  public List<String> findAllBrandOfDeviceTypes() {
    return deviceTypeRepository.findAllBrandOfDevicesType();
  }

  public boolean create(DeviceType deviceType) {
    if (deviceIsSaveable(deviceType)) {
        deviceTypeRepository.save(deviceType);
        return true;
    }
    return false;
  }
  
  private boolean deviceIsSaveable(DeviceType deviceType) {
    Optional<DeviceType> savedTypeOpt = deviceTypeRepository.findById(deviceType.getId());
    Optional<DeviceType> typeByNameOpt = deviceTypeRepository.findByNameIgnoreCase(deviceType.getName());
    Optional<DeviceType> typeByBrandAndModelOpt = deviceTypeRepository.findByBrandAndModelIgnoreCase(deviceType.getBrand(), deviceType.getModel());
      return 
          !(typeByNameOpt.isPresent() || typeByBrandAndModelOpt.isPresent()) || 
          (typeByNameOpt.isPresent() && typeByNameOpt.get().equals(savedTypeOpt.get())) ||
          (typeByBrandAndModelOpt.isPresent() && typeByBrandAndModelOpt.get().equals(savedTypeOpt.get()));
  }
  
  public Optional<DeviceType> findByName(String name) {
    return deviceTypeRepository.findByNameIgnoreCase(name);
  }

  public void update(DeviceType deviceType) {
    deviceTypeRepository.save(deviceType);
  }

  public List<DeviceType> findAllVisible() {
    return deviceTypeRepository.findAllByVisibleTrue();
  }
}
