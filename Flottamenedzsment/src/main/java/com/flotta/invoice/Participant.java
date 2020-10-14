package com.flotta.invoice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.entity.switchTable.ParticipantDescriptionCategoryCouplerST;

@Entity
@Table(name = "participants")
public class Participant {
  
  @Id
  @GeneratedValue
  private long id;
  
  @Column(unique = true)
  private String name;
  
  private String address;
  
  @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, ParticipantDescriptionCategoryCouplerST> participantDescriptionCategoryCouplersST = new HashMap<>();
  
  public Participant() {}

  public Participant(String name, String address) {
    this.name = name;
    this.address = address;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public Map<LocalDate, ParticipantDescriptionCategoryCouplerST> getParticipantDescriptionCategoryCouplersST() {
    return participantDescriptionCategoryCouplersST;
  }

  public void setParticipantDescriptionCategoryCouplersST(Map<LocalDate, ParticipantDescriptionCategoryCouplerST> participantDescriptionCategoryCouplersST) {
    if(participantDescriptionCategoryCouplersST == null) {
      this.participantDescriptionCategoryCouplersST = new HashMap<>();
    } else {
      this.participantDescriptionCategoryCouplersST = participantDescriptionCategoryCouplersST;
    }
  }

}
