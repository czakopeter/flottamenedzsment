package com.flotta.service.registry;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.invoice.ChargeRatioByCategory;
import com.flotta.model.registry.Device;
import com.flotta.model.registry.DeviceType;
import com.flotta.model.registry.Sim;
import com.flotta.model.registry.Subscription;
import com.flotta.model.registry.User;
import com.flotta.model.viewEntity.DeviceToView;
import com.flotta.model.viewEntity.SubscriptionToView;
import com.flotta.utility.MessageToView;

@Service
public class RegistryService {

  private DeviceService deviceService;

  private DeviceTypeService deviceTypeService;

  private SimService simService;

  private SubscriptionService subscriptionService;

  private UserService userService;

  @Autowired
  public RegistryService(DeviceService deviceService, DeviceTypeService deviceTypeService, SimService simService, SubscriptionService subscriptionService, UserService userService) {
    this.deviceService = deviceService;
    this.deviceTypeService = deviceTypeService;
    this.simService = simService;
    this.subscriptionService = subscriptionService;
    this.userService = userService;
  }

//  public User getCurrentUser() {
//    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    return userService.findByEmail(((UserDetailsImpl) auth.getPrincipal()).getUsername());
//  }

//-------- DEVICE SERVICE --------

  public List<Device> findAllDevices() {
    return deviceService.findAll();
  }

  //TODO
  public boolean createDevice(DeviceToView dtv) {
    DeviceType deviceType = deviceTypeService.findByName(dtv.getTypeName());
    if (deviceType != null && deviceService.add(dtv)) {
      Device saved = deviceService.findBySerialNumber(dtv.getSerialNumber());
      saved.setDeviceType(deviceType);
      deviceService.save(saved);
      return true;
    }
    return false;
  }

  public boolean updateDevice(DeviceToView dtv) {
    Optional<Device> optional = deviceService.findById(dtv.getId());
    if(optional.isPresent()) {
      Device device = optional.get();
      Optional<User> user = userService.findById(dtv.getUserId());
      
      device.addUser(user, dtv.getBeginDate());
      device.addNote(dtv.getNote(), dtv.getBeginDate());
      deviceService.save(device);
    }
    
    return optional.isPresent();
  }

  public Optional<Device> findDeviceById(long id) {
    return deviceService.findById(id);
  }

//  public DeviceToView findDeviceByIdAndDate(long id, LocalDate date) {
//    return deviceService.findById(id).get().toView(date);
//  }

//  public List<LocalDate> findDeviceDatesById(long id) {
//    return deviceService.findById(id).get().getAllModificationDateDesc();
//  }

  public String getDeviceServiceError() {
    return deviceService.getError();
  }

//-------- DEVICE TYPE SERVICE --------

  public List<DeviceType> findAllDeviceTypes() {
    return deviceTypeService.findAll();
  }

  public List<String> findAllBrandOfDevicesType() {
    return deviceTypeService.findAllBrandOfDevicesType();
  }

  public boolean saveDeviceType(DeviceType deviceType) {
    return deviceTypeService.save(deviceType);
  }

  public DeviceType findDeviceTypeById(long id) {
    return deviceTypeService.findById(id);
  }

  public void updateDeviceType(DeviceType deviceType) {
    deviceTypeService.update(deviceType);
  }

  public List<DeviceType> findAllVisibleDeviceTypes() {
    return deviceTypeService.findAllVisibleDeviceTypes();
  }

//-------- SIM SERVICE --------

  public List<Sim> findAllFreeSim() {
    return simService.findAllFree();
  }

  public List<Sim> findAllSim() {
    return simService.findAll();
  }

  public Optional<Sim> findSimById(int id) {
    return simService.findById(id);
  }

  public List<String> getSimChangeReasons() {
    return simService.getAllChagneReason();
  }

  public boolean createSim(Sim sim) {
    return simService.create(sim);
  }

  public String getSimError() {
    return simService.removeMsg();
  }

  public boolean canCreateSubscription() {
    return !simService.findAllFree().isEmpty();
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

  public List<LocalDate> findSubscriptionDatesById(long id) {
    Optional<Subscription> optional = subscriptionService.findById(id);
    if (optional.isPresent()) {
      return optional.get().getAllModificationDateDesc();
    } else {
      return Collections.emptyList();
    }
  }

  public boolean createSubscription(SubscriptionToView stv) {
    Optional<Sim> simOpt = simService.findByImei(stv.getImei());
    return subscriptionService.create(stv, simOpt);
  }

  public boolean updateSubscription(SubscriptionToView stv) {
    Optional<Subscription> optional = subscriptionService.findById(stv.getId());
    if (optional.isPresent()) {
      Subscription sub = optional.get();
      Device dev = deviceService.findById(stv.getDeviceId()).orElse(null);

      sub.addSim(simService.findByImei(stv.getImei()), stv.getSimChangeReason(), stv.getBeginDate());
      sub.addUser(userService.findById(stv.getUserId()), stv.getBeginDate());
      sub.addDevice(dev, stv.getBeginDate());
      sub.addNote(stv.getNote(), stv.getBeginDate());
      subscriptionService.update(sub);
    }

    return optional.isPresent();
  }

  public String getSubscriptionServiceError() {
    return subscriptionService.removeMsg();
  }

//TODO put UserServiceImp function here
  // -------- USER SERVICE --------
  public boolean registerUser(User user) {
    if (userService.registerUser(user)) {
      return true;
    }
    return false;
  }

  public List<User> findAllUser() {
    return userService.findAll();
  }

  public Optional<User> findUserByEmail(String email) {
    return userService.findByEmail(email);
  }

  public boolean changePassword(String email, String oldPsw, String newPsw, String confirmPsw) {
    return userService.changePassword(email, oldPsw, newPsw, confirmPsw);
  }

  public List<MessageToView> getUserError() {
    MessageToView mtv = new MessageToView(userService.removeMsg());
    mtv.setToDanger();
    return Arrays.asList(mtv);
  }

  public boolean firstAdminRegistration(User user) {
    return userService.firstAdminRegistration(user);
  }

  public boolean registrationAvailable() {
    return userService.registrationAvailable();
  }

  public boolean activation(String key) {
    return userService.activation(key);
  }

  public List<User> findAllUserByStatus(int status) {
    return userService.findAllByStatus(status);
  }

  public Optional<User> findUserById(long id) {
    return userService.findById(id);
  }

  public boolean updateUser(long id, Map<String, Boolean> roles) {
    return userService.updateUser(id, roles);
  }

  public boolean requestNewPassword(String email) {
    return userService.requestNewPassword(email);
  }

  public boolean updateChargeRatioOfUser(long userId, ChargeRatioByCategory chargeRatio) {
    return userService.editChargeRatioOfUser(userId, chargeRatio);
  }
}
