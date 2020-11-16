package com.flotta.service.record;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.entity.record.Device;
import com.flotta.entity.record.User;
import com.flotta.entity.viewEntity.DeviceToView;
import com.flotta.repository.record.DeviceRepository;
import com.flotta.utility.Utility;

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

//  public void userHasConnected(Device dev, LocalDate date) {
//    deviceStatusService.setStatus(dev, DeviceStatusEnum.ACTIVE, date);
//  }
//
//  public void userHasntConnected(Device dev, LocalDate date) {
//    deviceStatusService.setStatus(dev, DeviceStatusEnum.FREE, date);
//  }
//
//  public void deleteLastSatus(Device dev) {
//    deviceStatusService.deleteLastStatus(dev);
//  }

  
  //TODO nem kell az összes eszköz, csak ami valaha a felhasználónál volt és aktív
  public List<DeviceToView> findAllCurrentByUser(User user) {
    List<DeviceToView> result = new LinkedList<DeviceToView>();
    List<Device> all = deviceRepository.findAll();
    for(Device d : all) {
      if(Utility.isSameByIdOrBothNull(user, d.getActualUser())) {
        result.add(d.toView());
      }
    }
    return result;
  }

  public void save(Device device) {
    deviceRepository.save(device);
  }
}
