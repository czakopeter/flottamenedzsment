package com.flotta.model.registry;

import java.time.LocalDate;

import javax.persistence.MappedSuperclass;

import com.flotta.model.BasicEntity;

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
