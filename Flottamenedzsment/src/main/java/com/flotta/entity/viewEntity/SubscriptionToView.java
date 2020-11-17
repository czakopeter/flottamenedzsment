package com.flotta.entity.viewEntity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.flotta.entity.note.SubNote;
import com.flotta.entity.record.Device;
import com.flotta.entity.record.Sim;
import com.flotta.entity.record.User;
import com.flotta.entity.switchTable.BasicSwitchTable;
import com.flotta.entity.switchTable.SubDev;
import com.flotta.entity.switchTable.SubSim;
import com.flotta.entity.switchTable.UserSub;
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

  private String min;

  public SubscriptionToView() {
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

  public String getMin() {
    return min;
  }

  public void setMin(String min) {
    this.min = min;
  }

  @Override
  public String toString() {
    return "SubscriptionToView [number=" + number + ", imei=" + imei + ", simChangeReason=" + simChangeReason + ", userId=" + userId + ", userName=" + userName + ", deviceId=" + deviceId + ", deviceName=" + deviceName + ", beginDate=" + beginDate + ", min=" + min + "]";
  }

  public void setUser(BasicSwitchTable bst) {
    if(bst == null || !(bst instanceof UserSub)) {
      this.userId = 0;
      this.userName = "";
    } else {
      User user = ((UserSub) bst).getUser();
      this.userId = user != null ? user.getId() : 0;
      this.userName = user != null ? user.getFullName() : "";
    }
  }

  public void setSim(BasicSwitchTable bst) {
    if(bst == null || !(bst instanceof SubSim)) {
      this.oldImei = "";
      this.imei = "";
    } else {
      Sim sim = ((SubSim) bst).getSim();
      this.oldImei = sim != null ? sim.getImei() : "";
      this.imei = sim != null ? sim.getImei() : "";
    }
  }

  public void setDevice(BasicSwitchTable bst) {
    if(bst == null || !(bst instanceof SubDev)) {
      this.deviceId = 0;
      this.deviceName = "";
    } else { 
      Device device = ((SubDev)bst).getDev();
      this.deviceId = device != null ? device.getId() : 0;
      this.deviceName = device != null ? device.getDeviceType().getName() : "";
    }
  }

  public void setNote(BasicSwitchTable bst) {
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

}
