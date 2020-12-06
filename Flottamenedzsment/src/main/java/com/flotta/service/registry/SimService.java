package com.flotta.service.registry;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.enums.MessageKey;
import com.flotta.enums.MessageType;
import com.flotta.enums.SimStatus;
import com.flotta.model.registry.Sim;
import com.flotta.repository.registry.SimRepository;
import com.flotta.utility.ExtendedBoolean;

@Service
public class SimService {
  
	private SimRepository simRepository;
	
	@Autowired
	public void setSimRepository(SimRepository simRepository) {
    this.simRepository = simRepository;
  }
	
  public List<Sim> findAll() {
    return simRepository.findAll(); 
  }
	
	public List<Sim> findAllByStatus(SimStatus status) {
    return simRepository.findAllByStatus(status);
	}

	public Optional<Sim> findById(long id) {
		return simRepository.findById(id);
	}
	
	public Optional<Sim> findByImei(String imei) {
    return simRepository.findByImei(imei);
	}

  //TODO check imei, pin, puk format
  public ExtendedBoolean create(Sim sim) {
    ExtendedBoolean eb = new ExtendedBoolean(true);
//    if(!Validator.checkImieWithLuhnAlg(sim.getImei())) {
//      eb.setInvalid();
//      eb.addMessage(MessageKey.IMEI_NUMBER_NOT_VALID, MessageType.WARNING);
//      return eb;
//    }
    Optional<Sim> simOpt = simRepository.findByImei(sim.getImei());
    if(simOpt.isPresent()) {
      eb.setInvalid();
      eb.addMessage(MessageKey.IMEI_ALREADY_USED, MessageType.WARNING);
    } else {
      sim.setStatus(SimStatus.FREE);
      simRepository.save(sim);
    }
    return eb;
  }
}
