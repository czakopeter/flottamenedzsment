package com.flotta.entity.record;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.flotta.entity.BasicEntity;
import com.flotta.entity.switchTable.SubSim;
import com.flotta.enums.SimStatusEnum;

@Entity
@Table(name = "sims")
public class Sim extends BasicEntity {

  @Column(unique = true)
  private String imei;
  
  private String pin;
  
  private String puk;
  
  private String reason;  

  @OneToOne(mappedBy = "sim")
  @JoinColumn(name = "sub_id")
  private SubSim simSub;

  private SimStatusEnum status;
  
//  @OneToMany(mappedBy = "sim", cascade = CascadeType.ALL)
//  @MapKey(name = "date")
//  private Map<LocalDate, SimStatus> stats = new HashMap<LocalDate, SimStatus>();

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

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public SubSim getSimSub() {
    return simSub;
  }

  public void setSimSub(SubSim simSub) {
    this.simSub = simSub;
  }

//  public Map<LocalDate, SimStatus> getStats() {
//    return stats;
//  }
//
//  public void setStats(Map<LocalDate, SimStatus> stats) {
//    this.stats = stats;
//  }
//
//  public boolean isFree() {
//    List<LocalDate> dates = new LinkedList<>(stats.keySet());
//    Collections.sort(dates, Collections.reverseOrder());
//    return stats.get(dates.get(0)).isFree();
//  }
//
//  public void addStatus(SimStatusEnum status, LocalDate date) {
//    stats.put(date, new SimStatus(status, this, date));
//  }
//
//  public void removeLastStatus() {
//    if(!stats.isEmpty()) {
//      List<LocalDate> dates = new LinkedList<>(stats.keySet());
//      Collections.sort(dates, Collections.reverseOrder());
//      stats.remove(dates.get(0));
//      
//    }
//  }
//  
//  public String getStatus() {
//    List<LocalDate> dates = new LinkedList<>(stats.keySet());
//    Collections.sort(dates, Collections.reverseOrder());
//    return stats.get(dates.get(0)).getStatus().toString();
//  }
  
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