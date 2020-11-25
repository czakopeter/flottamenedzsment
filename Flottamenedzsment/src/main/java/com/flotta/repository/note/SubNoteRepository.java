package com.flotta.repository.note;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.note.SubNote;
import com.flotta.model.registry.Subscription;

public interface SubNoteRepository extends CrudRepository<SubNote, Long>{

  List<SubNote> findAllBySub(Subscription sub);

  SubNote findFirstBySubOrderByBeginDateDesc(Subscription sub);

  SubNote findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(Subscription sub, LocalDate date);

}
