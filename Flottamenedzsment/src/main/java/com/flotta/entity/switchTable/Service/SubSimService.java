package com.flotta.entity.switchTable.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.entity.record.Sim;
import com.flotta.entity.record.Subscription;
import com.flotta.entity.switchTable.SubSim;
import com.flotta.entity.switchTable.repository.SubSimRepository;
import com.flotta.service.SimService;
import com.flotta.service.SubscriptionService;

@Service
public class SubSimService {

  private SubSimRepository subSimRepository;
  
  private SubscriptionService subscriptionService;
  
  private SimService simService;

  @Autowired
  SubSimService(SubSimRepository subSimRepository, SubscriptionService subscriptionService, SimService simService) {
    this.subSimRepository = subSimRepository;
    this.subscriptionService = subscriptionService;
    this.simService = simService;
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
      last.getSim().setReason(simChangeReason);
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
  

//  public void update(long subId, Long simId, LocalDate date, String imeiChangeReason) {
//    SubSim last = subSimRepository.findFirstBySubOrderByConnectDesc(subscriptionService.findById(subId));
//    switch (imeiChangeReason) {
//    case "CHANGED":
//    case "STOLE":
//    case "LOST":
//      if(date.isEqual(last.getConnect())) {
//        //utolsó visszaállítása FREE állapotba + hozzá tartozó SubSim törlése
//        simService.removeLastStatusModification(last.getSim().getId());
//        last.setSim(null);
//        last.setSub(null);
//        subSimRepository.delete(last);
//        //az új utolsó cseréje okának módosítása
//        last = subSimRepository.findFirstBySubOrderByConnectDesc(subscriptionService.findById(subId));
//        simService.modifySimLastStatus(last.getSim().getId(), imeiChangeReason);
//        //új SubSim létrehozása a paraméterek szerint + sim aktiválása
//        subSimRepository.save(new SubSim(subscriptionService.findById(subId), simService.findById(simId), date));
//        simService.findById(simId).addStatus(SimStatusEnum.ACTIVE, date);
//      } else {
//        last.getSim().addStatus(SimStatusEnum.valueOf(imeiChangeReason), date);
//        subSimRepository.save(new SubSim(subscriptionService.findById(subId), simService.findById(simId), date));
//        simService.findById(simId).addStatus(SimStatusEnum.ACTIVE, date);
//      }
//      break;
//    default:
//      break;
//    }
//  }
}
