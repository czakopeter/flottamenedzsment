package com.flotta.repository.note;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.note.DevNote;
import com.flotta.model.registry.Device;

public interface DevNoteRepository extends CrudRepository<DevNote, Long>{

  List<DevNote> findAllByDev(Device dev);

  DevNote findFirstByDevOrderByBeginDateDesc(Device dev);

  DevNote findFirstByDevAndBeginDateBeforeOrderByBeginDateDesc(Device dev, LocalDate date);

}
