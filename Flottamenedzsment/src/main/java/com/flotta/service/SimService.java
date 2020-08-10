package com.flotta.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.entity.Sim;
import com.flotta.enums.SimStatusEnum;
import com.flotta.repository.SimRepository;
import com.flotta.status.service.SimStatusService;

@Service
public class SimService extends ServiceWithMsg {
  
  private static String[] changeReasons = {"CHANGED", "STOLE", "LOST"};
	
	private SimRepository simRepository;
	
	private SimStatusService simStatusService;
	
	@Autowired
	public void setSimRepository(SimRepository simRepository) {
    this.simRepository = simRepository;
  }
	
	@Autowired
  public void setSimStatusService(SimStatusService simStatusService) {
    this.simStatusService = simStatusService;
  }

  public List<Sim> findAll() {
    return simRepository.findAll(); 
  }
	
	public List<Sim> findAllFree() {
	  List<Sim> result = simRepository.findAllBySimSubIsNullAndReasonIsNull();
    return result;
	}

	public Sim findById(long id) {
		return simRepository.findById(id).orElse(null);
	}
	
	public Sim findByImei(String imei) {
    return simRepository.findByImei(imei);
	}

//  public void save(Sim sim, LocalDate date) {
//    Sim check = simRepository.findByImei(sim.getImei());
//    if(check == null) {
//      sim.addStatus(SimStatusEnum.FREE, date);
//      simRepository.save(sim);
//    }
//  }
//
//  public void removeLastStatusModification(long id) {
//    Sim sim = simRepository.findOne(id);
//    if(sim != null) {
//     simStatusService.deleteLastStatus(sim);
//    }
//  }

//  public void modifySimLastStatus(long simId, String imeiChangeReason) {
//    Sim sim = simRepository.findOne(simId);
//    if(sim != null && !sim.isFree()) {
//      simStatusService.modifyLastStatus(sim, imeiChangeReason);
//    }
//  }

  //TODO check imei, pin, puk format
  public boolean add(Sim sim) {
//    if(Validator.isValidImieWithLuhnAlg(sim.getImei())) {
//      appendMsg("Imei " + sim.getImei() + " is not valid!");
//      return false;
//    }
    Sim check = simRepository.findByImei(sim.getImei());
    if(check == null) {
      sim.setStatus(SimStatusEnum.FREE);
      simRepository.save(sim);
      return true;
    } else {
      appendMsg("Imei " + sim.getImei() + " already exists!");
      return false;
    }
  }

  public List<String> getAllChagneReason() {
    return Arrays.asList(changeReasons);
  }
}
