package com.flotta.service.note;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flotta.model.note.SubNote;
import com.flotta.model.registry.Subscription;
import com.flotta.repository.note.SubNoteRepository;

@Service
public class SubNoteService {

  private SubNoteRepository subNoteRepository;
  
  @Autowired
  public void setSubNoteRepository(SubNoteRepository subNoteRepository) {
    this.subNoteRepository = subNoteRepository;
  }

  public void save(Subscription sub, String note, LocalDate date) {
    List<SubNote> list = subNoteRepository.findAllBySub(sub);
    if(list.isEmpty()) {
      subNoteRepository.save(new SubNote(sub, note, date));
    }
  }

  public void update(Subscription sub, String note, LocalDate date) {
    SubNote last = subNoteRepository.findFirstBySubOrderByBeginDateDesc(sub);
    
    if(date.isAfter(last.getBeginDate())) {
      if((last.getNote().isEmpty()) ||
          (note.isEmpty()) ||
          !note.equalsIgnoreCase(last.getNote())) {
        subNoteRepository.save(new SubNote(sub, note, date));
      }
    } else if(date.isEqual(last.getBeginDate())) {
      //modifying
      SubNote lastBefore = subNoteRepository.findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(sub, date);
      if(lastBefore != null && (
          note.equalsIgnoreCase(lastBefore.getNote())
          )) {
        subNoteRepository.deleteById(last.getId());
      } else {
        last.setNote(note);
        subNoteRepository.save(last);
      }
    } else {
      //error
    }
  }

}
