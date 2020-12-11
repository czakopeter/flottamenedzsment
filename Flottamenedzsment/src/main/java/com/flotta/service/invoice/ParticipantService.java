package com.flotta.service.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.model.invoice.Participant;
import com.flotta.repository.invoice.ParticipantRepository;
import com.flotta.utility.BooleanWithMessages;

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

  @Override
  public Optional<Participant> findByName(String name) {
    return participantRepository.findByName(name);
  }

  BooleanWithMessages create(Participant participant) {
    BooleanWithMessages eb = new BooleanWithMessages(true);
    Optional<Participant> participantOpt = participantRepository.findByName(participant.getName());
    if (!participantOpt.isPresent()) {
      participantRepository.save(participant);
    } else {
      eb.setFalse();
      eb.addMessage(MessageKey.PARTICIPANT_NAME_ALREADY_USED, MessageType.WARNING);
    }
    return eb;
  }

  void update(Participant participant) {
    participantRepository.save(participant);
  }
}
