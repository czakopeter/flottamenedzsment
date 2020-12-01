package com.flotta.model.viewEntity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.flotta.model.note.SubNote;
import com.flotta.model.registry.Device;
import com.flotta.model.registry.Sim;
import com.flotta.model.registry.Subscription;
import com.flotta.model.registry.User;
import com.flotta.model.switchTable.BasicSwitchTable;
import com.flotta.model.switchTable.SubDev;
import com.flotta.model.switchTable.SubSim;
import com.flotta.model.switchTable.UserSub;
import com.flotta.utility.Utility;

public class SubscriptionToView {
  private long id;

  private String number;

  private String oldImei;

  private String imei;

  private String simChangeReason;

  private long userId;

  private String userName;

  private long deviceId;

  private String deviceName;

  private String note;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate beginDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  public SubscriptionToView() {}
  
  public SubscriptionToView(Subscription subscription) {
    id = subscription.getId();
    number = subscription.getNumber();
    beginDate = getLastModificationDateOfDevice(subscription);

    setSim(Utility.getBasicSwitchTable(subscription.getSubSim()));

    setUser(Utility.getBasicSwitchTable(subscription.getSubUsers()));

    setDevice(Utility.getBasicSwitchTable(subscription.getSubDev()));

    setNote(Utility.getBasicSwitchTable(subscription.getNotes()));
  }
  
  public SubscriptionToView(Subscription subscription, LocalDate date) {
    id = subscription.getId();
    number = subscription.getNumber();
    beginDate = date;

    setSim(Utility.getBasicSwitchTable(subscription.getSubSim(), date));

    setUser(Utility.getBasicSwitchTable(subscription.getSubUsers(), date));

    setDevice(Utility.getBasicSwitchTable(subscription.getSubDev(), date));

    setNote(Utility.getBasicSwitchTable(subscription.getNotes(), date));  
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getOldImei() {
    return oldImei;
  }

  public void setOldImei(String oldImei) {
    this.oldImei = oldImei;
  }

  public String getImei() {
    return imei;
  }

  public void setImei(String imei) {
    this.imei = imei;
  }

  public String getSimChangeReason() {
    return simChangeReason;
  }

  public void setSimChangeReason(String simChangeReason) {
    this.simChangeReason = simChangeReason;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public long getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(long deviceId) {
    this.deviceId = deviceId;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public LocalDate getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(LocalDate beginDate) {
    this.beginDate = beginDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  private void setUser(BasicSwitchTable bst) {
    if(bst == null || !(bst instanceof UserSub)) {
      this.userId = 0;
      this.userName = "";
    } else {
      User user = ((UserSub) bst).getUser();
      this.userId = user != null ? user.getId() : 0;
      this.userName = user != null ? user.getFullName() : "";
    }
  }

  private void setSim(BasicSwitchTable bst) {
    if(bst == null || !(bst instanceof SubSim)) {
      this.oldImei = "";
      this.imei = "";
    } else {
      Sim sim = ((SubSim) bst).getSim();
      this.oldImei = sim != null ? sim.getImei() : "";
      this.imei = sim != null ? sim.getImei() : "";
    }
  }

  private void setDevice(BasicSwitchTable bst) {
    if(bst == null || !(bst instanceof SubDev)) {
      this.deviceId = 0;
      this.deviceName = "";
    } else { 
      Device device = ((SubDev)bst).getDev();
      this.deviceId = device != null ? device.getId() : 0;
      this.deviceName = device != null ? device.getDeviceType().getName() : "";
    }
  }

  private void setNote(BasicSwitchTable bst) {
    if(bst == null || !(bst instanceof SubNote)) {
      this.note = "";
    } else {
      String note = ((SubNote) bst).getNote();
      this.note = note != null ? note : "";
    }
  }

  public String getPeriod() {
    return Utility.getPeriod(beginDate, endDate);
  }
  
  private LocalDate getLastModificationDateOfDevice(Subscription subscription) {
    return subscription.getAllModificationDateDesc().get(0);
  }

}
