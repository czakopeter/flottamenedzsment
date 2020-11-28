package com.flotta.service.switchTable;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.Sim;
import com.flotta.model.registry.Subscription;
import com.flotta.model.switchTable.SubSim;
import com.flotta.repository.switchTable.SubSimRepository;

@Service
public class SubSimService {

  private SubSimRepository subSimRepository;
  

  @Autowired
  SubSimService(SubSimRepository subSimRepository) {
    this.subSimRepository = subSimRepository;
  }
  
  public List<SubSim> findAllBySub(Subscription s) {
    return subSimRepository.findAllBySub(s);
  }
  
  public SubSim findBySubAndConnect(Subscription s, LocalDate date) {
    return subSimRepository.findBySubAndBeginDate(s, date);
  }
  
  public void delete(long id) {
    subSimRepository.deleteById(id);
  }
  
  public void delete(Subscription s, LocalDate date) {
    subSimRepository.deleteBySubAndBeginDate(s, date);
  }

  public void save(Subscription s, Sim sim, LocalDate date) {
    subSimRepository.save(new SubSim(s, sim, date));
  }
  
  public void update(Subscription sub, Sim sim, LocalDate date, String simChangeReason) {
    SubSim last = subSimRepository.findFirstBySubOrderByBeginDateDesc(sub);
    
    //first sim connection
    if(!equals(last, sub, sim) && last.getSim() == null) {
      if(date.isAfter(last.getBeginDate())) {
        subSimRepository.save(new SubSim(sub, sim, date));
      } else {
        last.setSim(sim);
        subSimRepository.save(last);
      }
    } else if(!equals(last, sub, sim) && date.isAfter(last.getBeginDate())) {
      last.getSim().setChangeReason(simChangeReason);
      subSimRepository.save(last);
      subSimRepository.save(new SubSim(sub, sim, date));
    }
  }
  
  private boolean equals(SubSim ss, Subscription sub, Sim sim) {
    if(ss.getSim() == null && sim == null) {
      return true;
    }
    if(ss.getSim() == null || sim == null) {
      return false;
    }
    return ss.getSim().getImei().equalsIgnoreCase(sim.getImei());
  }

}
