package com.flotta.status;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.flotta.entity.Device;
import com.flotta.enums.DeviceStatusEnum;


@Entity
@Table(name = "dev_status")
public class DeviceStatus {

  @Id
  @GeneratedValue
  private long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  private Device dev;
  
  private DeviceStatusEnum status;
  
  private LocalDate date;
  
  public DeviceStatus() {
  }

  public DeviceStatus(DeviceStatusEnum status, Device dev, LocalDate date) {
    this.status = status;
    this.dev = dev;
    this.date = date;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Device getDev() {
    return dev;
  }

  public void setDev(Device dev) {
    this.dev = dev;
  }

  public DeviceStatusEnum getStatus() {
    return status;
  }

  public void setStatus(DeviceStatusEnum status) {
    this.status = status;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public boolean isFree() {
    return status.isFree();
  }

}
