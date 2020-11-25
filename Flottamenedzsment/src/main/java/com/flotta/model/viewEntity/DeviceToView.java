package com.flotta.model.viewEntity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.flotta.model.note.DevNote;
import com.flotta.model.registry.Subscription;
import com.flotta.model.registry.User;
import com.flotta.model.switchTable.BasicSwitchTable;
import com.flotta.model.switchTable.SubDev;
import com.flotta.model.switchTable.UserDev;
import com.flotta.model.switchTable.UserSub;
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

  public void setUser(BasicSwitchTable bst) {
    if(bst == null || !(bst instanceof UserDev)) {
      this.userId = 0;
      this.userName = "";
    } else {
      User user = ((UserDev)bst).getUser();
      this.userId = user != null ? user.getId() : 0;
      this.userName = user != null ? user.getFullName() : "";
    }
  }

  public void setSubscription(BasicSwitchTable bst) {
    if(bst == null || !(bst instanceof SubDev)) {
      this.number = "";
    } else {
      Subscription subscription = ((SubDev) bst).getSub();
      this.number = subscription != null ? subscription.getNumber() : "";
    }
  }
  
  public void setNote(BasicSwitchTable bst) {
    if(bst == null || !(bst instanceof DevNote)) {
      this.note = "";
    } else {
      String note = ((DevNote) bst).getNote();
      this.note = note != null ? note : "";
    }
  }
  
  public String getPeriod() {
    return Utility.getPeriod(beginDate, endDate);
  }
}