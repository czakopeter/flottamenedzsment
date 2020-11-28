package com.flotta.service.registry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.Device;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.repository.registry.DeviceRepository;

@Service
public class DeviceService extends ServiceWithMsg{
  
  private DeviceRepository deviceRepository;
  
//  private DeviceStatusService deviceStatusService;

  @Autowired
  public void setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
  }
  
//  @Autowired
//  public void setDeviceStatusService(DeviceStatusService deviceStatusService) {
//    this.deviceStatusService = deviceStatusService;
//  }

  public List<Device> findAll() {
    return deviceRepository.findAll();
  }
  
  public Device findById(long id) {
    return deviceRepository.findById(id).orElse(null);
  }
  
  public Device findBySerialNumber(String selialNumber) {
    if (selialNumber != null) {
      return deviceRepository.findBySerialNumber(selialNumber);
    }
    return null;
  }
  
  public boolean add(DeviceToView dtv) {
    if(deviceRepository.findBySerialNumber(dtv.getSerialNumber()) == null) {
      Device entity = new Device(dtv.getSerialNumber(), dtv.getBeginDate());
      deviceRepository.save(entity);
      return true;
    } else {
      appendMsg("Serial number already exists");
      return false;
    }
  }
  
  public String getError() {
    return removeMsg();
  }

  public void save(Device device) {
    deviceRepository.save(device);
  }
}
