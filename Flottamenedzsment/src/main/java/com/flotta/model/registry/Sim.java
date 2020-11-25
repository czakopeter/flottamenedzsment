package com.flotta.model.registry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flotta.enums.SimStatusEnum;
import com.flotta.model.BasicEntity;
import com.flotta.model.switchTable.SubSim;

@Entity
@Table(name = "sims")
public class Sim extends BasicEntity {

  @Column(unique = true)
  private String imei;
  
  private String pin;
  
  private String puk;
  
  private String changeReason;  

  @OneToOne(mappedBy = "sim")
  @JoinColumn(name = "sub_id")
  private SubSim simSub;

  private SimStatusEnum status;
  
  public Sim() {
  }

  public String getImei() {
    return imei;
  }

  public void setImei(String imei) {
    this.imei = imei;
  }
  
  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public String getPuk() {
    return puk;
  }

  public void setPuk(String puk) {
    this.puk = puk;
  }

  public String getChangeReason() {
    return changeReason;
  }

  public void setChangeReason(String changeReason) {
    this.changeReason = changeReason;
  }

  public SubSim getSimSub() {
    return simSub;
  }

  public void setSimSub(SubSim simSub) {
    this.simSub = simSub;
  }

  public SimStatusEnum getStatus() {
    return status;
  }
  
  public void setStatus(SimStatusEnum status) {
    this.status = status;
  }

  public static boolean isSameByIdOrBothNull(Sim s1, Sim s2) {
    if(s1 == null && s2 == null) {
      return true;
    }
    if(s1 == null || s2 == null) {
      return false;
    }
    return s1.equals(s2);
  }

}