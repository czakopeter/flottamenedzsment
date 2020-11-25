package com.flotta.model.note;

import java.security.InvalidParameterException;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.flotta.model.registry.Device;
import com.flotta.model.switchTable.BasicSwitchTable;
import com.flotta.utility.Utility;

@Entity
@Table( name = "dev_note" )
public class DevNote extends BasicSwitchTable {

  private String note;

  @ManyToOne
  private Device dev;


  public DevNote() {
  }

  public DevNote(Device dev, String note, LocalDate date) {
    this.dev = dev;
    this.note = note;
    this.beginDate = date;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Device getDev() {
    return dev;
  }

  public void setDev(Device dev) {
    this.dev = dev;
  }
  
  @Override
  public <Other extends BasicSwitchTable> boolean isSameSwitchedPairs(Other other) {
    if(other == null) {
      throw new NullPointerException();
    }
    if(!(other instanceof DevNote)) {
      throw new InvalidParameterException();
    }
    DevNote act = (DevNote)other;
    
    return (act.note == null && this.note == null || this.note.equals(act.note)) && Utility.isSameByIdOrBothNull(this.dev, act.dev);
  }
  
}
