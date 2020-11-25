package com.flotta.service.note;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.note.DevNote;
import com.flotta.model.registry.Device;
import com.flotta.repository.note.DevNoteRepository;

@Service
public class DevNoteService {

  private DevNoteRepository devNoteRepository;
  
  @Autowired
  public void setDevNoteRepository(DevNoteRepository devNoteRepository) {
    this.devNoteRepository = devNoteRepository;
  }

  public void save(Device dev, String note, LocalDate date) {
    List<DevNote> list = devNoteRepository.findAllByDev(dev);
    if(list.isEmpty()) {
      devNoteRepository.save(new DevNote(dev, note, date));
    }
  }

  public void update(Device dev, String note, LocalDate date) {
    DevNote last = devNoteRepository.findFirstByDevOrderByBeginDateDesc(dev);
    if(last == null) {
      save(dev, note, date);
      return;}
    
    if(date.isAfter(last.getBeginDate())) {
      if((last.getNote().isEmpty()) ||
          (note.isEmpty()) ||
          !note.equalsIgnoreCase(last.getNote())) {
        devNoteRepository.save(new DevNote(dev, note, date));
      }
    } else if(date.isEqual(last.getBeginDate())) {
      //modifying
      DevNote lastBefore = devNoteRepository.findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(dev, date);
      if(lastBefore != null && (
          note.equalsIgnoreCase(lastBefore.getNote())
          )) {
        devNoteRepository.deleteById(last.getId());
      } else {
        last.setNote(note);
        devNoteRepository.save(last);
      }
    } else {
      //error
    }
  }

  public DevNote findLastNote(Device device) {
    return devNoteRepository.findFirstByDevOrderByBeginDateDesc(device);
  }

}
