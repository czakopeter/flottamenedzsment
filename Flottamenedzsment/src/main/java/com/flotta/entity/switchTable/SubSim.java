package com.flotta.entity.switchTable;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.flotta.entity.record.Sim;
import com.flotta.entity.record.Subscription;
import com.flotta.utility.Utility;

@Entity
@Table( name="sub_sim_st")
public class SubSim extends BasicSwitchTable {
  
  @ManyToOne(cascade = CascadeType.ALL)
//  @ManyToOne
  @JoinColumn( name = "sub_id")
  private Subscription sub;
  
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn( name = "sim_id")
  private Sim sim;
  
  public SubSim() {
  }

  public SubSim(Subscription sub, Sim sim, LocalDate beginDate) {
    this.sub = sub;
    this.sim = sim;
    this.beginDate = beginDate;
  }

  public Subscription getSub() {
    return sub;
  }

  public void setSub(Subscription sub) {
    this.sub = sub;
  }

  public Sim getSim() {
    return sim;
  }

  public void setSim(Sim sim) {
    this.sim = sim;
  }

  @Override
  public <Other extends BasicSwitchTable> boolean isSameSwitchedPairs(Other other) {
    if(other == null) {
      throw new NullPointerException();
    }
    if(!(other instanceof SubSim)) {
      throw new InvalidParameterException();
    }
    SubSim act = (SubSim)other;
    
    return Utility.isSameByIdOrBothNull(this.sub, act.sub) && Utility.isSameByIdOrBothNull(this.sim, act.sim);
  }
}
