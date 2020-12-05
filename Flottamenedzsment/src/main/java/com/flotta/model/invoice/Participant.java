package com.flotta.model.invoice;

import java.util.Comparator;

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
  
  public static Comparator<Participant> BY_NAME = 
      Comparator.comparing(Participant::getName, String.CASE_INSENSITIVE_ORDER);
  
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
