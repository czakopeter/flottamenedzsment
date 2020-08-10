package com.flotta.entity.note.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.Subscription;
import com.flotta.entity.note.SubNote;

public interface SubNoteRepository extends CrudRepository<SubNote, Long>{

  List<SubNote> findAllBySub(Subscription sub);

  SubNote findFirstBySubOrderByBeginDateDesc(Subscription sub);

  SubNote findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(Subscription sub, LocalDate date);

}
