package com.flotta.entity.switchTable;

import java.time.LocalDate;

import javax.persistence.MappedSuperclass;

import org.springframework.format.annotation.DateTimeFormat;

import com.flotta.entity.BasicEntity;

@MappedSuperclass
public abstract class BasicSwitchTable extends BasicEntity {

  @DateTimeFormat (pattern="yyyy-MM-dd")
  protected LocalDate beginDate;
  
  @DateTimeFormat (pattern="yyyy-MM-dd")
  protected LocalDate endDate;

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

  public abstract <Other extends BasicSwitchTable> boolean isSameSwitchedPairs(Other other);
}
