package com.flotta.repository.note;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.entity.note.DevNote;
import com.flotta.entity.record.Device;

public interface DevNoteRepository extends CrudRepository<DevNote, Long>{

  List<DevNote> findAllByDev(Device dev);

  DevNote findFirstByDevOrderByBeginDateDesc(Device dev);

  DevNote findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(Device dev, LocalDate date);

}
