package com.flotta.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.invoice.Participant;

public interface ParticipantRepository extends CrudRepository<Participant, Long> {
  
  List<Participant> findAll();

  Participant findByName(String name);

}
