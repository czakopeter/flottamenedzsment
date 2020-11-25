package com.flotta.entity.invoice;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BasicFeeItem extends BasicAccountElement {

  protected String subscription;
  
  protected String description;

  public String getSubscription() {
    return subscription;
  }

  public void setSubscription(String subscription) {
    this.subscription = subscription;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
}
