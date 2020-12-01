package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.Device;
import com.flotta.model.registry.DeviceType;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.repository.registry.DeviceRepository;

@Service
public class DeviceService extends ServiceWithMsg{
  
  private DeviceRepository deviceRepository;
  
  @Autowired
  public void setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
  }
  
  public List<Device> findAll() {
    return deviceRepository.findAll();
  }
  
  public Optional<Device> findById(long id) {
    return deviceRepository.findById(id);
  }
  
  public boolean create(DeviceToView dtv, Optional<DeviceType> deviceTypeOpt ) {
    if(deviceRepository.findBySerialNumber(dtv.getSerialNumber()).isPresent()) {
      appendMsg("Serial number already exists");
    } else {
      if(deviceTypeOpt.isPresent()) {
        Device entity = new Device(dtv.getSerialNumber(), deviceTypeOpt.get() ,dtv.getBeginDate());
        deviceRepository.save(entity);
        return true;
      }
    }
    return false;
  }
  
  public void update(Device device) {
    deviceRepository.save(device);
  }
  
  public String getError() {
    return removeMsg();
  }
}
