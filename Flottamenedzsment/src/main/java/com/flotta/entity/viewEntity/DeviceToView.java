package com.flotta.entity.viewEntity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.flotta.entity.record.Subscription;
import com.flotta.entity.record.User;
import com.flotta.utility.Utility;

public class DeviceToView {
  private long id;
  
	private String serialNumber;
	
	private String typeName;
	
	private long userId;
	
	private String userName;
	
	private String number;
	
	private String note;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate beginDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate endDate;
	
	private String min;
	
	public DeviceToView() {
	}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }
  
  public String getNote() {
    return note;
  }
  
  public void setNote(String note) {
    this.note = note;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
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

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
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
    return "DeviceToView [serialNumber=" + serialNumber + ", typeName=" + typeName + ", beginDate=" + beginDate + ", min=" + min + "]";
  }

  public void setUser(User user) {
    this.userId = user != null ? user.getId() : 0;
    this.userName = user != null ? user.getFullName() : "";
  }

  public void setSubscription(Subscription sub) {
    this.number = sub != null ? sub.getNumber() : "";
  }
  
  public String getPeriod() {
    return Utility.getPeriod(beginDate, endDate);
  }

}
