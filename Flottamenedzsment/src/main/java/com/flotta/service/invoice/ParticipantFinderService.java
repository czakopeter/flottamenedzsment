package com.flotta.service.invoice;

import java.util.List;
import java.util.Optional;

import com.flotta.model.invoice.Participant;

public interface ParticipantFinderService {
  
  public List<Participant> findAll() ;

  public Optional<Participant> findById(long id);
  
  public Optional<Participant> findByName(String name);
}
