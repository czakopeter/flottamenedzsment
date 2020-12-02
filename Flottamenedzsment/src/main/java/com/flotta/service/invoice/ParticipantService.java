package com.flotta.service.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.invoice.DescriptionCategoryCoupler;
import com.flotta.model.invoice.Participant;
import com.flotta.repository.invoice.ParticipantRepository;

@Service
public class ParticipantService implements ParticipantFinderService {
  
  private ParticipantRepository participantRepository;
  
  @Autowired
  public void setParticipantRepository(ParticipantRepository participantRepository) {
    this.participantRepository = participantRepository;
  }

  @Override
  public List<Participant> findAll() {
    return participantRepository.findAll();
  }

  @Override
  public Optional<Participant> findById(long id) {
    return participantRepository.findById(id);
  }
  
  public Optional<Participant> findByName(String name) {
    return participantRepository.findByName(name);
  }

  public boolean create(Participant participant, Optional<DescriptionCategoryCoupler> dccOpt) {
    if(dccOpt.isPresent()) {
      Optional<Participant> participantOpt = participantRepository.findByName(participant.getName());
      if(!participantOpt.isPresent()) {
        participant.setDescriptionCategoryCoupler(dccOpt.get());
        participantRepository.save(participant);
      }
      return !participantOpt.isPresent();
    }
    return false;
  }
  
  public void update(Participant participant) {
    participantRepository.save(participant);
    
  }
}
