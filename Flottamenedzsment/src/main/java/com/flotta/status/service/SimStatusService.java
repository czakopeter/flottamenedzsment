package com.flotta.status.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.enums.SimStatusEnum;
import com.flotta.model.registry.Sim;
import com.flotta.status.SimStatus;
import com.flotta.status.repository.SimStatusRepository;

@Service
public class SimStatusService {

  private SimStatusRepository simStatusRepository;

  @Autowired
  public void setSimStatusRepository(SimStatusRepository simStatusRepository) {
    this.simStatusRepository = simStatusRepository;
  }

  public SimStatus findById(long id) {
    return simStatusRepository.findById(id).orElse(null);
  }

  public List<SimStatus> findAllBySim(Sim sim) {
    return simStatusRepository.findAllBySim(sim);
  }

  public boolean save(Sim sim, String status, LocalDate date) {
    try {
      simStatusRepository.save(new SimStatus(SimStatusEnum.valueOf(status), sim, date));
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }
  
  public void deleteLastStatus(Sim sim) {
    SimStatus status = simStatusRepository.findFirstBySimOrderByDateDesc(sim);
    if(status != null && !status.isFree()) {
      simStatusRepository.delete(status);
    }
  }
  
//  public void deleteAllStatus(Sim sim) {
//    simStatusRepository.deleteAllBySim(sim);
//  }

  public void modifyLastStatus(Sim sim, String imeiChangeReason) {
    SimStatus ss = simStatusRepository.findFirstBySimOrderByDateDesc(sim);
    ss.setStatus(SimStatusEnum.valueOf(imeiChangeReason));
    simStatusRepository.save(ss);
  }
  
}
