package com.flotta.service.registry;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.enums.Availability;
import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.enums.SimStatus;
import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.model.registry.Device;
import com.flotta.model.registry.DeviceType;
import com.flotta.model.registry.Sim;
import com.flotta.model.registry.Subscription;
import com.flotta.model.registry.User;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.model.viewEntity.SubscriptionToView;
import com.flotta.utility.BooleanWithMessages;

@Service
public class RegistryManager {

  private DeviceService deviceService;

  private DeviceTypeService deviceTypeService;

  private SimService simService;

  private SubscriptionService subscriptionService;

  private UserService userService;

  @Autowired
  public RegistryManager(DeviceService deviceService, DeviceTypeService deviceTypeService, SimService simService, SubscriptionService subscriptionService, UserService userService) {
    this.deviceService = deviceService;
    this.deviceTypeService = deviceTypeService;
    this.simService = simService;
    this.subscriptionService = subscriptionService;
    this.userService = userService;
  }

//-------- DEVICE SERVICE --------

  public List<Device> findAllDevices() {
    return deviceService.findAll();
  }
  
  public Optional<Device> findDeviceById(long id) {
    return deviceService.findById(id);
  }

  public BooleanWithMessages createDevice(DeviceToView dtv) {
    Optional<DeviceType> deviceTypeOpt = deviceTypeService.findByName(dtv.getTypeName());
    return deviceService.create(dtv, deviceTypeOpt);
  }

  public BooleanWithMessages updateDevice(DeviceToView dtv) {
    Optional<Device> deviceOpt = deviceService.findById(dtv.getId());
    BooleanWithMessages eb = new BooleanWithMessages(deviceOpt.isPresent());
    deviceOpt.ifPresent(device -> {
      device.addUser(userService.findById(dtv.getUserId()), dtv.getBeginDate());
      device.addNote(dtv.getNote(), dtv.getBeginDate());
      deviceService.update(device);
    });
    if(!eb.booleanValue()) {
      eb.addMessage(MessageKey.UNKNOWN_DEVICE, MessageType.ERROR);
    }
    return eb;
  }

//-------- DEVICE TYPE SERVICE --------

  public List<DeviceType> findAllDeviceTypes() {
    return deviceTypeService.findAll();
  }

  public List<String> findAllBrandOfDevicesType() {
    return deviceTypeService.findAllBrandOfDeviceTypes();
  }

  public BooleanWithMessages saveDeviceType(DeviceType deviceType) {
    return deviceTypeService.create(deviceType);
  }

  public Optional<DeviceType> findDeviceTypeById(long id) {
    return deviceTypeService.findById(id);
  }

  public void updateDeviceType(DeviceType deviceType) {
    deviceTypeService.update(deviceType);
  }

  public List<DeviceType> findAllDeviceTypesByAvailability(Availability availability) {
    return deviceTypeService.findAllByAvailability(availability);
  }
  
  public BooleanWithMessages canCreateDevice() {
    BooleanWithMessages eb = new BooleanWithMessages(!deviceTypeService.findAll().isEmpty());
    if(!eb.booleanValue()) {
      eb.addMessage(MessageKey.NO_DEVICE_TYPE, MessageType.WARNING);
    }
    return eb; 
  }

//-------- SIM SERVICE --------

  public List<Sim> findAllFreeSim() {
    return simService.findAllByStatus(SimStatus.FREE);
  }

  public List<Sim> findAllSim() {
    return simService.findAll();
  }

  public Optional<Sim> findSimById(int id) {
    return simService.findById(id);
  }

  public BooleanWithMessages createSim(Sim sim) {
    return simService.create(sim);
  }

  public BooleanWithMessages canCreateSubscription() {
    BooleanWithMessages eb = new BooleanWithMessages(!simService.findAllByStatus(SimStatus.FREE).isEmpty());
    if(!eb.booleanValue()) eb.addMessage(MessageKey.NO_FREE_SIM, MessageType.WARNING);
    return eb;
  }

//------- SUBSCRIPTION SERVICE --------

  public List<Subscription> findAllSubscription() {
    return subscriptionService.findAll();
  }

  public Optional<Subscription> findSubscriptionById(long id) {
    return subscriptionService.findById(id);
  }

  public Optional<Subscription> findSubscriptionByNumber(String number) {
    return subscriptionService.findByNumber(number);
  }

  public BooleanWithMessages createSubscription(SubscriptionToView stv) {
    Optional<Sim> simOpt = simService.findByImei(stv.getImei());
    return subscriptionService.create(stv, simOpt);
  }

  public BooleanWithMessages updateSubscription(SubscriptionToView stv) {
      Optional<Subscription> subscriptonOpt = subscriptionService.findById(stv.getId());
      BooleanWithMessages eb = new BooleanWithMessages(subscriptonOpt.isPresent());
        subscriptonOpt.ifPresent(subscription -> {
          subscription.addSim(simService.findByImei(stv.getImei()), stv.getSimChangeReason(), stv.getBeginDate());
          subscription.addUser(userService.findById(stv.getUserId()), stv.getBeginDate());
          subscription.addDevice(deviceService.findById(stv.getDeviceId()), stv.getBeginDate());
          subscription.addNote(stv.getNote(), stv.getBeginDate());
          subscriptionService.update(subscription);
        });
    return eb;
  }

  // -------- USER SERVICE --------
  public BooleanWithMessages createUser(User user) {
    return userService.create(user);
  }

  public List<User> findAllUser() {
    return userService.findAll();
  }

  public Optional<User> findUserByEmail(String email) {
    return userService.findByEmail(email);
  }

  public BooleanWithMessages changePassword(String email, String oldPsw, String newPsw, String confirmPsw) {
    return userService.changePassword(email, oldPsw, newPsw, confirmPsw);
  }

  public BooleanWithMessages createFirstAdmin(User user) {
    return userService.createFirstAdmin(user);
  }

  public BooleanWithMessages activation(String key) {
    return userService.activation(key);
  }

  public Optional<User> findUserById(long id) {
    return userService.findById(id);
  }

  public BooleanWithMessages updateUser(User user, Map<String, Boolean> roles) {
    return userService.update(user, roles);
  }

  public BooleanWithMessages requestNewPassword(String email) {
    return userService.requestNewPassword(email);
  }

  public boolean updateChargeRatioOfUser(long userId, Optional<ChargeRatioByCategory> chargeRatioOpt) {
    return userService.updateChargeRatioOfUser(userId, chargeRatioOpt);
  }
  
  public boolean hasAdmin() {
    return userService.hasAdmin();
  }
  
  public boolean hasEnabledAdmin() {
    return userService.hasEnabledAdmin();
  }

  public BooleanWithMessages deleteUserById(long id) {
    return userService.delete(id);
  }
}
