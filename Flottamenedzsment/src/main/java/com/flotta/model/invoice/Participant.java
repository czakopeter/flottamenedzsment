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
  
//  @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
//  @MapKey(name = "beginDate")
//  private Map<LocalDate, ParticipantDescriptionCategoryCouplerST> participantDescriptionCategoryCouplersST = new HashMap<>();
  
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

//  public Map<LocalDate, ParticipantDescriptionCategoryCouplerST> getParticipantDescriptionCategoryCouplersST() {
//    return participantDescriptionCategoryCouplersST;
//  }

//  public void setParticipantDescriptionCategoryCouplersST(Map<LocalDate, ParticipantDescriptionCategoryCouplerST> participantDescriptionCategoryCouplersST) {
//    if(participantDescriptionCategoryCouplersST == null) {
//      this.participantDescriptionCategoryCouplersST = new HashMap<>();
//    } else {
//      this.participantDescriptionCategoryCouplersST = participantDescriptionCategoryCouplersST;
//    }
//  }
  
  public DescriptionCategoryCoupler getDescriptionCategoryCoupler() {
    return descriptionCategoryCoupler;
  }

  public void setDescriptionCategoryCoupler(DescriptionCategoryCoupler descriptionCategoryCoupler) {
    this.descriptionCategoryCoupler = descriptionCategoryCoupler;
  }

}
