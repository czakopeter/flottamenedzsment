package com.flotta.service.registry;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.enums.SimStatusEnum;
import com.flotta.model.registry.Sim;
import com.flotta.repository.registry.SimRepository;

@Service
public class SimService extends ServiceWithMsg {
  
  private static String[] changeReasons = {"CHANGED", "STOLE", "LOST"};
	
	private SimRepository simRepository;
	
	@Autowired
	public void setSimRepository(SimRepository simRepository) {
    this.simRepository = simRepository;
  }
	
  public List<Sim> findAll() {
    return simRepository.findAll(); 
  }
	
	public List<Sim> findAllFree() {
	  List<Sim> result = simRepository.findAllByStatus(SimStatusEnum.FREE);
    return result;
	}

	public Optional<Sim> findById(long id) {
		return simRepository.findById(id);
	}
	
	public Optional<Sim> findByImei(String imei) {
    return simRepository.findByImei(imei);
	}

  //TODO check imei, pin, puk format
  public boolean create(Sim sim) {
//    if(Validator.isValidImieWithLuhnAlg(sim.getImei())) {
//      appendMsg("Imei " + sim.getImei() + " is not valid!");
//      return false;
//    }
    Optional<Sim> simOpt = simRepository.findByImei(sim.getImei());
    if(simOpt.isPresent()) {
      appendMsg("Imei " + sim.getImei() + " already exists!");
    } else {
      sim.setStatus(SimStatusEnum.FREE);
      simRepository.save(sim);
    }
    return !simOpt.isPresent();
  }

  public List<String> getAllChagneReason() {
    return Arrays.asList(changeReasons);
  }
}
