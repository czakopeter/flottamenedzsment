package com.flotta.entity.switchTable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.flotta.invoice.Participant;
import com.flotta.invoice.DescriptionCategoryCoupler;

@Entity
public class ParticipantDescriptionCategoryCouplerST extends BasicSwitchTable {

  @ManyToOne
  private Participant participant;
  
//  private DescriptionCategoryCoupler descriptionCategoryCoupler;

  public Participant getParticipant() {
    return participant;
  }
  
  
  public void setParticipant(Participant participant) {
    this.participant = participant;
  }

//  public DescriptionCategoryCoupler getDescriptionCategoryCoupler() {
//    return descriptionCategoryCoupler;
//  }
//
//
//
//  public void setDescriptionCategoryCoupler(DescriptionCategoryCoupler descriptionCategoryCoupler) {
//    this.descriptionCategoryCoupler = descriptionCategoryCoupler;
//  }



  @Override
  public <Other extends BasicSwitchTable> boolean isSameSwitchedPairs(Other other) {
    return false;
  }

}
