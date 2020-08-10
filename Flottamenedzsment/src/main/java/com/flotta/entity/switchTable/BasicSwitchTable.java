package com.flotta.entity.switchTable;

import java.time.LocalDate;

import javax.persistence.MappedSuperclass;

import com.flotta.entity.BasicEntity;

@MappedSuperclass
public abstract class BasicSwitchTable extends BasicEntity {

  protected LocalDate beginDate;

  public LocalDate getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(LocalDate beginDate) {
    this.beginDate = beginDate;
  }
  
  public abstract <Other extends BasicSwitchTable> boolean isSameSwitchedPairs(Other other);
}
