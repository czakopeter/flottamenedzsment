package com.flotta.model.invoice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.flotta.model.BasicEntity;

@Entity
@Table(name = "participants")
public class Participant extends BasicEntity {
  
  @Column(unique = true)
  private String name;
  
  private String address;
  
  @ManyToOne
  private DescriptionCategoryCoupler descriptionCategoryCoupler;
  
  public Participant() {}

  public Participant(String name, String address) {
    this.name = name;
    this.address = address;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public DescriptionCategoryCoupler getDescriptionCategoryCoupler() {
    return descriptionCategoryCoupler;
  }

  public void setDescriptionCategoryCoupler(DescriptionCategoryCoupler descriptionCategoryCoupler) {
    this.descriptionCategoryCoupler = descriptionCategoryCoupler;
  }

}
