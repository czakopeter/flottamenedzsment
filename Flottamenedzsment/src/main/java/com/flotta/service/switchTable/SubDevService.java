package com.flotta.service.switchTable;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.registry.Device;
import com.flotta.model.registry.Subscription;
import com.flotta.model.switchTable.SubDev;
import com.flotta.repository.switchTable.SubDevRepository;
import com.flotta.utility.Utility;

@Service
public class SubDevService {

  private SubDevRepository subDevRepository;

  @Autowired
  public void setUserSubRepository(SubDevRepository userSubRepository) {
    this.subDevRepository = userSubRepository;
  }

  public void save(Subscription sub, Device dev, LocalDate date) {
    SubDev entity = new SubDev(sub, dev, date);
    subDevRepository.save(entity);
  }

  public void updateFromSubscription(Subscription sub, Device dev, LocalDate date) {
    if (sub == null) {
      throw new NullPointerException();
    }

    SubDev lastFromSub = subDevRepository.findFirstBySubOrderByBeginDateDesc(sub);

    if (Utility.isSameByIdOrBothNull(dev, lastFromSub.getDev())) {
      return;
    }

    closeDevice(dev, date);
    
    if (date.isAfter(lastFromSub.getBeginDate())) {
      if (lastFromSub.getDev() != null) {
        subDevRepository.save(new SubDev(null, lastFromSub.getDev(), date));
      }
      subDevRepository.save(new SubDev(sub, dev, date));
    } else if (date.isEqual(lastFromSub.getBeginDate())) {
      
    }

    if (dev == null) {
      if (date.isEqual(lastFromSub.getBeginDate())) {
        if (lastFromSub.getDev() == null) {
          // nothing
        } else {
          SubDev lastBeforeFromSub = subDevRepository.findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(sub, date);
          SubDev lastBeforeFromDev = subDevRepository.findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(lastFromSub.getDev(), date);

          if (lastBeforeFromSub == null && lastBeforeFromDev == null) {
            subDevRepository.save(new SubDev(sub, null, date));
            lastFromSub.setSub(null);
            subDevRepository.save(lastFromSub);
          }
          if (lastBeforeFromSub == null && lastBeforeFromDev != null) {
            if (lastBeforeFromDev.getSub() == null) {
              lastFromSub.setDev(null);
              subDevRepository.save(lastFromSub);
            } else {
              subDevRepository.save(new SubDev(null, lastFromSub.getDev(), date));
              lastFromSub.setDev(null);
              subDevRepository.save(lastFromSub);
            }
          }
          if (lastBeforeFromSub != null && lastBeforeFromDev == null) {
            if (lastBeforeFromSub.getDev() == null) {
              lastFromSub.setSub(null);
              subDevRepository.save(lastFromSub);
            } else {
              subDevRepository.save(new SubDev(sub, null, date));
              lastFromSub.setSub(null);
              subDevRepository.save(lastFromSub);
            }
          }
          if (lastBeforeFromSub != null && lastBeforeFromDev != null) {
            subDevRepository.deleteById(lastFromSub.getId());
            if (lastBeforeFromSub.getDev() != null) {
              subDevRepository.save(new SubDev(sub, null, date));
            }
            if (lastBeforeFromDev.getSub() != null) {
              subDevRepository.save(new SubDev(null, lastFromSub.getDev(), date));
            }
          }
        }
      }
    }
    if (dev != null) {
      if (date.isEqual(lastFromSub.getBeginDate())) {
        if (Utility.isSameByIdOrBothNull(lastFromSub.getDev(), dev)) {
          // nothing
        } else if (lastFromSub.getDev() == null) {
          closeDevice(dev, date);
          SubDev lastBeforeFromSub = subDevRepository.findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(sub, date);
          if (lastBeforeFromSub == null) {
            lastFromSub.setDev(dev);
            subDevRepository.save(lastFromSub);
          } else {
            if (Utility.isSameByIdOrBothNull(dev, lastBeforeFromSub.getDev())) {
              System.out.println("IDE " + lastFromSub.getId());
              subDevRepository.deleteById(lastFromSub.getId());
            } else {
              lastFromSub.setDev(dev);
              subDevRepository.save(lastFromSub);
            }
          }
        } else {
          closeDevice(dev, date);
          SubDev lastBeforeFromSub = subDevRepository.findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(sub, date);
          SubDev lastBeforeFromDev = subDevRepository.findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(lastFromSub.getDev(), date);

          if (lastBeforeFromSub == null && lastBeforeFromDev == null) {
            subDevRepository.save(new SubDev(sub, dev, date));
            lastFromSub.setSub(null);
            subDevRepository.save(lastFromSub);
          }
          if (lastBeforeFromSub == null && lastBeforeFromDev != null) {
            if (lastBeforeFromDev.getSub() != null) {
              subDevRepository.save(new SubDev(sub, dev, date));
            }
            lastFromSub.setDev(dev);
            subDevRepository.save(lastFromSub);
          }
          if (lastBeforeFromSub != null && lastBeforeFromDev == null) {
            if (!Utility.isSameByIdOrBothNull(dev, lastBeforeFromSub.getDev())) {
              subDevRepository.save(new SubDev(sub, dev, date));
            }
            lastFromSub.setSub(null);
            subDevRepository.save(lastFromSub);
          }
          if (lastBeforeFromSub != null && lastBeforeFromDev != null) {
            if (lastBeforeFromDev.getSub() != null) {
              subDevRepository.save(new SubDev(null, lastFromSub.getDev(), date));
            }
            if (Utility.isSameByIdOrBothNull(lastBeforeFromSub.getDev(), dev)) {
              subDevRepository.deleteById(lastFromSub.getId());
            } else {
              lastFromSub.setDev(dev);
              subDevRepository.save(lastFromSub);
            }
          }
        }
      }
    }
  }

  private void closeDevice(Device dev, LocalDate date) {
    if (dev == null) {
      return;
    }

    SubDev last = subDevRepository.findFirstByDevOrderByBeginDateDesc(dev);

    if (date.isAfter(last.getBeginDate())) {
      if (last.getSub() != null) {
        subDevRepository.save(new SubDev(last.getSub(), null, date));
      }
    } else if (date.isEqual(last.getBeginDate())) {
      if (last.getSub() == null) {
        subDevRepository.delete(last);
      } else {
        last.setDev(null);
        subDevRepository.save(last);
      }
    }
  }

  public Subscription findLastSub(Device device) {
    return subDevRepository.findFirstByDevOrderByBeginDateDesc(device).getSub();
  }
  
}
