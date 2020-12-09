package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.registry.Device;
import com.flotta.model.registry.DeviceType;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.repository.registry.DeviceRepository;
import com.flotta.utility.ExtendedBoolean;

@Service
public class DeviceService {
  
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
  
  public ExtendedBoolean create(DeviceToView dtv, Optional<DeviceType> deviceTypeOpt ) {
    ExtendedBoolean eb = new ExtendedBoolean(true);
    if(deviceRepository.findBySerialNumber(dtv.getSerialNumber()).isPresent()) {
      eb.setInvalid();
      eb.addMessage(MessageKey.SERIAL_NUMBER_ALREADY_USED, MessageType.WARNING);
    } else {
      if(deviceTypeOpt.isPresent()) {
        Device entity = new Device(dtv.getSerialNumber(), deviceTypeOpt.get() ,dtv.getBeginDate());
        deviceRepository.save(entity);
      }
    }
    return eb;
  }
  
  public void update(Device device) {
    deviceRepository.save(device);
  }
  
}
