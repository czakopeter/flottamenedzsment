package com.flotta.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.entity.record.DeviceType;
import com.flotta.repository.DeviceTypeRepository;

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

  public List<String> findAllBrandOfDevicesType() {
    return deviceTypeRepository.findAllBrandOfDevicesType();
  }

  public boolean save(DeviceType deviceType) {
    boolean saveable = deviceIsSaveable(deviceType);
    if (saveable) {
        deviceTypeRepository.save(deviceType);
    }
    return saveable;
  }
  
  private boolean deviceIsSaveable(DeviceType deviceType) {
    DeviceType name = deviceTypeRepository.findByNameIgnoreCase(deviceType.getName());
    DeviceType brandAndModel = deviceTypeRepository.findByBrandAndModelIgnoreCase(deviceType.getBrand(), deviceType.getModel());
    if(deviceType.getId() == null) {
      return name == null && brandAndModel == null;
    } else {
      DeviceType saved = deviceTypeRepository.findById(deviceType.getId()).orElse(null);
      if((saved.getDevices() != null && saved.getDevices().size() > 0) ||
         (name != null && name.getId() != saved.getId()) || 
         (brandAndModel != null && brandAndModel.getId() != saved.getId())) {
        return false;
      }
      return true;
    }
  }
  
  public DeviceType findByName(String name) {
    return deviceTypeRepository.findByNameIgnoreCase(name);
  }

  public void update(DeviceType deviceType) {
    deviceTypeRepository.save(deviceType);
  }

  public List<DeviceType> findAllVisibleDeviceTypes() {
    return deviceTypeRepository.findAllDeviceTypeByVisibleTrue();
  }
}
