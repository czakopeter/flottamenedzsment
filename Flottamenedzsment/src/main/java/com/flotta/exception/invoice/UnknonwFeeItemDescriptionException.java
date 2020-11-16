package com.flotta.exception.invoice;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class UnknonwFeeItemDescriptionException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 6078416202624522162L;

  private List<String> unknownDescriptions;
  
  public UnknonwFeeItemDescriptionException(Set<String> unknownFreeItemDesc) {
    this.unknownDescriptions = new LinkedList<String>(unknownFreeItemDesc);
  }

  public List<String> getUnknownDescriptions() {
    return unknownDescriptions;
  }
  
} 
