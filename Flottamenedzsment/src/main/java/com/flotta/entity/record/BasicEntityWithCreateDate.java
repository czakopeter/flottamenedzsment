package com.flotta.entity.record;

import java.time.LocalDate;

import javax.persistence.MappedSuperclass;

import com.flotta.entity.BasicEntity;

@MappedSuperclass
public class BasicEntityWithCreateDate extends BasicEntity {
  
  protected LocalDate createDate;

  public LocalDate getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDate createDate) {
    this.createDate = createDate;
  }
  
}
