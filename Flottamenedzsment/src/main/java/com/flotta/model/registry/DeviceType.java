package com.flotta.model.registry;

import java.util.Comparator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.model.BasicEntity;

@Entity
@Table(name = "deviceTypes")
public class DeviceType extends BasicEntity {

  private String brand;

  private String model;
  
  @Column(unique = true, nullable = false)
  private String name;

  private boolean visible;
  
  @OneToMany(mappedBy = "deviceType")
  private List<Device> devices;
  
  public static final Comparator<DeviceType> BY_NAME = (o1,o2) -> o1.name.compareTo(o2.name);

  //TODO description or attributes; sim type, battery, size ...
  
  public DeviceType() {}


  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand.trim().replaceAll("\\s+", " ");
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model.trim().replaceAll("\\s+", " ");
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name.trim().replaceAll("\\s+", " ");
  }

  public List<Device> getDevices() {
    return devices;
  }

  public void setDevices(List<Device> devices) {
    this.devices = devices;
  }

  @Override
  public String toString() {
    return "DeviceType [id=" + id + ", visible=" + visible + ", brand=" + brand + ", model=" + model + ", name=" + name + "]";
  }
  
}