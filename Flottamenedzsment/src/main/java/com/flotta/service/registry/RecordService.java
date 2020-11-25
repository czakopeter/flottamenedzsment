package com.flotta.service.registry;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.flotta.enums.SimStatusEnum;
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
public class RecordService {

  private DeviceService deviceService;

  private DeviceTypeService deviceTypeService;

  private SimService simService;

  private SubscriptionService subscriptionService;

  private UserService userService;

  @Autowired
  public RecordService(DeviceService deviceService, DeviceTypeService deviceTypeService, SimService simService, SubscriptionService subscriptionService, UserService userService) {
    this.deviceService = deviceService;
    this.deviceTypeService = deviceTypeService;
    this.simService = simService;
    this.subscriptionService = subscriptionService;
    this.userService = userService;
  }

  public User getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return userService.findByEmail(((UserDetailsImpl) auth.getPrincipal()).getUsername());
  }

//-------- DEVICE SERVICE --------

  public List<DeviceToView> findAllDevices() {
    List<DeviceToView> list = new LinkedList<>();
    deviceService.findAll().forEach(device -> list.add(device.toView()));
    return list;
  }

  public boolean saveDevice(DeviceToView dtv) {
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
    Device device = deviceService.findById(dtv.getId());
    User user = userService.findById(dtv.getUserId());

    device.addUser(user, dtv.getBeginDate());
    device.addNote(dtv.getNote(), dtv.getBeginDate());

    deviceService.save(device);
    return true;
  }

  public DeviceToView findDeviceById(long id) {
    Device device = deviceService.findById(id);
    return device != null ? device.toView() : null;
  }

  public DeviceToView findDeviceByIdAndDate(long id, LocalDate date) {
    return deviceService.findById(id).toView(date);
  }

  public List<LocalDate> findDeviceDatesById(long id) {
    return deviceService.findById(id).getAllModificationDateDesc();
  }

  public String getDeviceServiceError() {
    return deviceService.getError();
  }

//  public List<DeviceToView> findAllCurrentDeviceOfUser() {
//    return deviceService.findAllCurrentByUser(getCurrentUser());
//  }
//  
//  public List<DeviceToView> findAllCurrentDeviceOfUser(long userId) {
//    return deviceService.findAllCurrentByUser(userService.findById(userId));
//  }

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

  public Sim findSimById(int i) {
    return simService.findById(i);
  }

  public List<String> getSimChangeReasons() {
    return simService.getAllChagneReason();
  }

  public boolean addSim(Sim sim) {
    return simService.add(sim);
  }

  public String getSimError() {
    return simService.removeMsg();
  }

  public boolean canCreateSubscription() {
    return !simService.findAllFree().isEmpty();
  }

//------- SUBSCRIPTION SERVICE --------

  public List<SubscriptionToView> findAllSubscription() {
    List<SubscriptionToView> list = new LinkedList<>();
    for (Subscription s : subscriptionService.findAll()) {
      list.add(s.toView());
    }
    return list;
  }

  public Optional<Subscription> findSubscriptionById(long id) {
    return subscriptionService.findById(id);
  }

  public SubscriptionToView findSubscriptionByIdAndDate(long id, LocalDate date) {
    Optional<Subscription> optional = subscriptionService.findById(id);
    if (optional.isPresent()) {
      return optional.get().toView(date);
    }
    return null;
  }

  public Optional<Subscription> findSubscriptionByNumber(String number) {
    return subscriptionService.findByNumber(number);
  }

  public SubscriptionToView findSubscriptionByNumberAndDate(String number, LocalDate date) {
    Optional<Subscription> optional = subscriptionService.findByNumber(number);
    if (optional.isPresent()) {
      return optional.get().toView(date);
    }
    return null;
  }

  public List<LocalDate> findSubscriptionDatesById(long id) {
    Optional<Subscription> optional = subscriptionService.findById(id);
    if (optional.isPresent()) {
      return optional.get().getAllModificationDateDesc();
    } else {
      return Collections.emptyList();
    }
  }

  public boolean addSubscription(SubscriptionToView stv) {
    Sim sim = simService.findByImei(stv.getImei());
    if (sim != null && subscriptionService.add(stv)) {
      Subscription saved = subscriptionService.findByNumber(stv.getNumber()).get();
      sim.setStatus(SimStatusEnum.ACTIVE);
      saved.addSim(sim, null, stv.getBeginDate());
      subscriptionService.save(saved);
      return true;
    }
    return false;
  }

  public boolean updateSubscription(SubscriptionToView stv) {
    Optional<Subscription> optional = subscriptionService.findById(stv.getId());
    if (optional.isPresent()) {
      Subscription sub = optional.get();
      Sim sim = simService.findByImei(stv.getImei());
      User user = userService.findById(stv.getUserId());
      Device dev = deviceService.findById(stv.getDeviceId());

      sub.addSim(sim, stv.getSimChangeReason(), stv.getBeginDate());
      sub.addUser(user, stv.getBeginDate());
      sub.addDevice(dev, stv.getBeginDate());
      sub.addNote(stv.getNote(), stv.getBeginDate());
      subscriptionService.save(sub);
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

  public User findUserByEmail(String email) {
    return userService.findByEmail(email);
  }

  public boolean changePassword(String oldPsw, String newPsw, String confirmPsw) {
    return userService.changePassword(oldPsw, newPsw, confirmPsw);
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

  public User findUserById(long id) {
    return userService.findById(id);
  }

  public boolean updateUser(long id, Map<String, Boolean> roles) {
    return userService.updateUser(id, roles);
  }

  public boolean passwordReset(String email) {
    return userService.passwordReset(email);
  }

  public User editChargeRatioOfUser(long userId, ChargeRatioByCategory chargeRatio) {
    return userService.editChargeRatioOfUser(userId, chargeRatio);
  }
}
