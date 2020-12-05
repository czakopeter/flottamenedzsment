package com.flotta.model.registry;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.model.note.DevNote;
import com.flotta.model.switchTable.BasicSwitchTable;
import com.flotta.model.switchTable.SubDev;
import com.flotta.model.switchTable.UserDev;
import com.flotta.status.DeviceStatus;
import com.flotta.utility.Utility;

/**
 * @author CzP
 *
 */
@Entity
@Table(name = "devices")
public class Device extends BasicEntityWithCreateDate {

  @Column(unique = true, nullable = false)
  private String serialNumber;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private DeviceType deviceType;

  @OneToMany(mappedBy = "dev", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, UserDev> devUsers = new HashMap<>();

  @OneToMany(mappedBy = "dev", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, SubDev> devSubs = new HashMap<>();

  @OneToMany(mappedBy = "dev", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, DevNote> notes = new HashMap<>();

  @OneToMany(mappedBy = "dev", cascade = CascadeType.ALL)
  @MapKey(name = "date")
  private Map<LocalDate, DeviceStatus> statuses = new HashMap<LocalDate, DeviceStatus>();

  public Device() {
  }

  public Device(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public Device(String serialNumber, DeviceType deviceType, LocalDate date) {
    this.serialNumber = serialNumber;
    this.createDate = date;
    this.deviceType = deviceType;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber.toUpperCase();
  }

  public DeviceType getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(DeviceType deviceType) {
    this.deviceType = deviceType;
  }

  public Map<LocalDate, UserDev> getDevUsers() {
    return devUsers;
  }

  public void setDevUsers(Map<LocalDate, UserDev> devUsers) {
    this.devUsers = devUsers;
  }

  public Map<LocalDate, SubDev> getDevSubs() {
    return devSubs;
  }

  public void setDevSubs(Map<LocalDate, SubDev> devSubs) {
    this.devSubs = devSubs;
  }

  public Map<LocalDate, DevNote> getNotes() {
    return notes;
  }

  public void setNotes(Map<LocalDate, DevNote> notes) {
    this.notes = notes;
  }

  public Map<LocalDate, DeviceStatus> getStatuses() {
    return statuses;
  }

  public void setStatuses(Map<LocalDate, DeviceStatus> statuses) {
    this.statuses = statuses;
  }
  
  public void addUser(Optional<User> userOpt, LocalDate date) {
    if (devUsers.isEmpty()) {
      userOpt.ifPresent(user -> {
        devUsers.put(date, new UserDev(user, this, date));
      });
    } else {
      LocalDate lastUserModDate = Utility.getLatestDate(devUsers);
      UserDev last = devUsers.get(lastUserModDate);
      if(last.getEndDate() != null) {
        //Az utolsó hozzárendelést már lezárták jelenleg nem tartozik senkihez
        if(date.minusDays(1).isEqual(last.getEndDate())) {
          if(userOpt.isPresent()) {
            User user = userOpt.get();
            if(last.getUser().equals(user)) {
              last.setEndDate(null);
            } else {
              devUsers.put(date, new UserDev(user, this, date));
            }
          }
        } else if(date.minusDays(1).isAfter(last.getEndDate())) {
          userOpt.ifPresent(user -> {
            devUsers.put(date, new UserDev(user, this, date));
          });
        }
      } else {
        //Valakihez éppen hozzá van rendelve
        if(userOpt.isPresent()) {
          User user = userOpt.get();
          if(!last.getUser().equals(user)) {
            if(date.isAfter(lastUserModDate)) {
              last.setEndDate(date.minusDays(1));
              devUsers.put(date, new UserDev(user, this, date));
            }
          }
        } else {
          if(date.isAfter(lastUserModDate)) {
            last.setEndDate(date.minusDays(1));
          }
        }
      }
    }
  }

  public void addNote(String note, LocalDate date) {
    if(notes.isEmpty()) {
      if (note != null) {
        notes.put(date, new DevNote(this, note, date));
      }
    } else {
      LocalDate lastNoteModDate = Utility.getLatestDate(notes);
      DevNote last = notes.get(lastNoteModDate);
      if(last.getEndDate() != null) {
        if(date.minusDays(1).isEqual(last.getEndDate())) {
          if(note == null) {
          //nem csinálunk semmit
          } else if(last.getNote().equals(note)) {
            last.setEndDate(null);
          } else {
            notes.put(date, new DevNote(this, note, date));
          }
        } else if(date.minusDays(1).isAfter(last.getEndDate())) {
          if(note == null) {
            //nem csinálunk semmit
          } else {
            notes.put(date, new DevNote(this, note, date));
          }
        }
      } else {
        //Valakihez éppen hozzá van rendelve
        if(last.getNote().equals(note)) {
          //nem csinálunk semmit
        } else if(note != null) {
          if(date.isAfter(lastNoteModDate)) {
            last.setEndDate(date.minusDays(1));
            notes.put(date, new DevNote(this, note, date));
          } else if(date.isEqual(lastNoteModDate)) {
         // Módosítjuk az új felhasználóval vagy nem történik módosítás
          }
        } else {
          if(date.isAfter(lastNoteModDate)) {
            last.setEndDate(date.minusDays(1));
          } else if(date.isEqual(lastNoteModDate)) {
            // Még nem tudom mi történjen
          }
        }
      }
    }
  }

  @Override
  public String toString() {
    return "Device [id=" + id + ", serialNumber=" + serialNumber + ", deviceType=" + deviceType + "]";
  }

  /**
   * visszaadja a Device objektum létrehozásának és adatainak változásának dátumát csökkenő sorrendben
   * @return List<LocalDate>
   */
  public List<LocalDate> getAllModificationDateDesc() {
    Set<LocalDate> dates = new HashSet<>();
    dates.add(createDate);
    dates.addAll(getModificationDates(devUsers));
    dates.addAll(getModificationDates(devSubs));
    dates.addAll(getModificationDates(notes));

    List<LocalDate> result = new LinkedList<>(dates);
    Collections.sort(result, Collections.reverseOrder());
    return result;
  }
  
  
  /**
   * visszaadja a map-ben tárolt BasicSwitchTable-k egyedi kezdeti dátumait és a vég dátumai utáni első napokat
   * @param Map<LocalDate, ? extends BasicSwitchTable>
   * @return Set<LocalDate>
   */
  private Set<LocalDate> getModificationDates(Map<LocalDate, ? extends BasicSwitchTable> map) {
    Set<LocalDate> dates = new HashSet<>();
    for(BasicSwitchTable bst : map.values()) {
      dates.add(bst.getBeginDate());
      if(bst.getEndDate() != null) {
        dates.add(bst.getEndDate().plusDays(1));
      }
    }
    return dates;
  }
}
