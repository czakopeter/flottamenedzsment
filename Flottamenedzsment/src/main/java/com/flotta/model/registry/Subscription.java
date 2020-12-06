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
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.flotta.enums.SimStatus;
import com.flotta.model.note.SubNote;
import com.flotta.model.switchTable.BasicSwitchTable;
import com.flotta.model.switchTable.SubDev;
import com.flotta.model.switchTable.SubSim;
import com.flotta.model.switchTable.UserSub;
import com.flotta.status.SubscriptionStatus;
import com.flotta.utility.Utility;

@Entity
@Table(name = "subscriptions")
public class Subscription extends BasicEntityWithCreateDate {

  @Column(length = 9, unique = true, nullable = false)
  private String number;

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, SubSim> subSim = new HashMap<>();

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, UserSub> subUsers = new HashMap<>();

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, SubDev> subDev = new HashMap<>();

  @OneToMany(mappedBy = "sub", cascade = CascadeType.ALL)
  @MapKey(name = "beginDate")
  private Map<LocalDate, SubNote> notes = new HashMap<>();

  @OneToMany(mappedBy = "sub")
  @MapKey(name = "date")
  private Map<LocalDate, SubscriptionStatus> statuses = new HashMap<>();

  public Subscription() {
  }

  public Subscription(String number) {
    this.number = number;
  }

  public Subscription(String number, LocalDate date) {
    this.number = number;
    this.createDate = date;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Map<LocalDate, SubSim> getSubSim() {
    return subSim;
  }

  public void setSubSim(Map<LocalDate, SubSim> subSim) {
    this.subSim = subSim;
  }

  public Map<LocalDate, UserSub> getSubUsers() {
    return subUsers;
  }

  public void setSubUsers(Map<LocalDate, UserSub> subUsers) {
    this.subUsers = subUsers;
  }

  public Map<LocalDate, SubDev> getSubDev() {
    return subDev;
  }

  public void setSubDev(Map<LocalDate, SubDev> subDev) {
    this.subDev = subDev;
  }

  public Map<LocalDate, SubNote> getNotes() {
    return notes;
  }

  public void setNotes(Map<LocalDate, SubNote> notes) {
    this.notes = notes;
  }

  public Map<LocalDate, SubscriptionStatus> getStatuses() {
    return statuses;
  }

  public void setStatuses(Map<LocalDate, SubscriptionStatus> statuses) {
    this.statuses = statuses;
  }

  @Override
  public String toString() {
    return "Subscription [id=" + id + ", number=" + number + ", subSim=" + subSim + ", subUsers=" + subUsers + "]";
  }

  //TODO OPTIONAL-lel megold
  public void addSim(Optional<Sim> simOpt, String reason, LocalDate date) {
    Sim sim = simOpt.orElse(null);
    if (sim == null) {
      System.err.println("Never happened");
      return;
    }
    sim.setStatus(SimStatus.USED);
    if (subSim.isEmpty()) {
      subSim.put(date, new SubSim(this, sim, date));
    } else {
      LocalDate lastModDate = Utility.getLatestDate(subSim);
      SubSim last = subSim.get(lastModDate);
      if (last.getEndDate() == null) {
        // Valakihez éppen hozzá van rendelve
        if (last.getSim().equals(sim)) {
          // nem csinálunk semmit
        } else {
          if (date.isAfter(lastModDate)) {
            last.setEndDate(date.minusDays(1));
            last.getSim().setChangeReason(reason);
            last.getSim().setStatus(SimStatus.CHANGED);
            sim.setStatus(SimStatus.USED);
            subSim.put(date, new SubSim(this, sim, date));
          } else if (date.isEqual(lastModDate)) {
            // Módosítjuk az új simmel vagy nem történik módosítás
          }
        }
      }
    }
  }

  //TODO OPTIONAL-lel megold
  public void addUser(Optional<User> userOpt, LocalDate date) {
    User user = userOpt.orElse(null);
    if (subUsers.isEmpty()) {
      //Még nem rendelték felhasználóhoz
      if (user != null) {
        //Most hozzárendelik egy felhasználóhoz
        subUsers.put(date, new UserSub(user, this, date));
      }
    } else {
      //Már legalább egyszer hozzárendelték egy felhazsnálóhoz
      LocalDate lastUserModDate = Utility.getLatestDate(subUsers);
      UserSub last = subUsers.get(lastUserModDate);
      if(last.getEndDate() != null) {
        //Az utolsó hozzárendelést már lezárták jelenleg nem tartozik senkihez
        if(date.minusDays(1).isEqual(last.getEndDate())) {
          if(user == null) {
          //nem csinálunk semmit
          } else if(last.getUser().equals(user)) {
            last.setEndDate(null);
          } else {
            subUsers.put(date, new UserSub(user, this, date));
          }
        } else if(date.minusDays(1).isAfter(last.getEndDate())) {
          if(user == null) {
            //nem csinálunk semmit
          } else {
            subUsers.put(date, new UserSub(user, this, date));
          }
        }
      } else {
        //Valakihez éppen hozzá van rendelve
        if(last.getUser().equals(user)) {
          //nem csinálunk semmit
        } else if(user != null) {
          if(date.isAfter(lastUserModDate)) {
            last.setEndDate(date.minusDays(1));
            subUsers.put(date, new UserSub(user, this, date));
          } else if(date.isEqual(lastUserModDate)) {
         // Módosítjuk az új felhasználóval vagy nem történik módosítás
          }
        } else {
          if(date.isAfter(lastUserModDate)) {
            last.setEndDate(date.minusDays(1));
          } else if(date.isEqual(lastUserModDate)) {
            // Még nem tudom mi történjen
          }
        }
      }
    }
  }

  //TODO OPTIONAL-lel megold
  public void addDevice(Optional<Device> deviceOpt, LocalDate date) {
    Device device = deviceOpt.orElse(null);
    if (subDev.isEmpty()) {
      if (device != null) {
        subDev.put(date, new SubDev(this, device, date));
      }
    } else {
      LocalDate lastModDate = Utility.getLatestDate(subDev);
      SubDev last = subDev.get(lastModDate);
      if(last.getEndDate() != null) {
        if(date.minusDays(1).isEqual(last.getEndDate())) {
          if(device == null) {
          //nem csinálunk semmit
          } else if(last.getDev().equals(device)) {
            last.setEndDate(null);
          } else {
            subDev.put(date, new SubDev(this, device, date));
          }
        } else if(date.minusDays(1).isAfter(last.getEndDate())) {
          if(device == null) {
            //nem csinálunk semmit
          } else {
            subDev.put(date, new SubDev(this, device, date));
          }
        }
      } else {
        //Valakihez éppen hozzá van rendelve
        if(last.getDev().equals(device)) {
          //nem csinálunk semmit
        } else if(device != null) {
          if(date.isAfter(lastModDate)) {
            last.setEndDate(date.minusDays(1));
            subDev.put(date, new SubDev(this, device, date));
          } else if(date.isEqual(lastModDate)) {
         // Módosítjuk az új felhasználóval vagy nem történik módosítás
          }
        } else {
          if(date.isAfter(lastModDate)) {
            last.setEndDate(date.minusDays(1));
          } else if(date.isEqual(lastModDate)) {
            // Még nem tudom mi történjen
          }
        }
      }
    }
  }

  public void addNote(String note, LocalDate date) {
    if(notes.isEmpty()) {
      if (note != null) {
        notes.put(date, new SubNote(this, note, date));
      }
    } else {
      LocalDate lastNoteModDate = Utility.getLatestDate(notes);
      SubNote last = notes.get(lastNoteModDate);
      if(last.getEndDate() != null) {
        if(date.minusDays(1).isEqual(last.getEndDate())) {
          if(note == null) {
          //nem csinálunk semmit
          } else if(last.getNote().equals(note)) {
            last.setEndDate(null);
          } else {
            notes.put(date, new SubNote(this, note, date));
          }
        } else if(date.minusDays(1).isAfter(last.getEndDate())) {
          if(note == null) {
            //nem csinálunk semmit
          } else {
            notes.put(date, new SubNote(this, note, date));
          }
        }
      } else {
        //Valakihez éppen hozzá van rendelve
        if(last.getNote().equals(note)) {
          //nem csinálunk semmit
        } else if(note != null) {
          if(date.isAfter(lastNoteModDate)) {
            last.setEndDate(date.minusDays(1));
            notes.put(date, new SubNote(this, note, date));
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
  
  public List<LocalDate> getUserChangeDatesBetween(LocalDate beginDate, LocalDate endDate) {
    List<LocalDate> result = new LinkedList<>();
    List<LocalDate> dates = new LinkedList<>(getModificationDates(subUsers));
    Collections.sort(dates);
    for (LocalDate date : dates) {
      if (date.isAfter(beginDate) && !date.isAfter(endDate)) {
        result.add(date);
      }
    }
    return result;
  }

  public User getUserByDate(LocalDate date) {
    LocalDate floor = Utility.floorDate(new LinkedList<>(getModificationDates(subUsers)), date);
    if (floor == null) {
      return null;
    } else {
      return subUsers.get(floor) != null ? subUsers.get(floor).getUser() : null;
    }
  }

  /**
   * visszaadja a Subscription objektum létrehozásának és adatainak változásának
   * dátumát csökkenő sorrendben
   * 
   * @return List<LocalDate>
   */
  public List<LocalDate> getAllModificationDateDesc() {
    Set<LocalDate> dates = new HashSet<>();
    dates.add(createDate);
    dates.addAll(getModificationDates(subUsers));
    dates.addAll(getModificationDates(subSim));
    dates.addAll(getModificationDates(subDev));
    dates.addAll(getModificationDates(notes));

    List<LocalDate> result = new LinkedList<>(dates);
    Collections.sort(result, Collections.reverseOrder());
    return result;
  }

  /**
   * visszaadja a map-ben tárolt BasicSwitchTable-k egyedi kezdeti dátumait és a
   * vég dátumai utáni első napokat
   * 
   * @param Map<LocalDate, ? extends BasicSwitchTable>
   * @return Set<LocalDate>
   */
  private Set<LocalDate> getModificationDates(Map<LocalDate, ? extends BasicSwitchTable> map) {
    Set<LocalDate> dates = new HashSet<>();
    for (BasicSwitchTable bst : map.values()) {
      dates.add(bst.getBeginDate());
      if (bst.getEndDate() != null) {
        dates.add(bst.getEndDate().plusDays(1));
      }
    }
    return dates;
  }

  public Set<User> getUsersBetween(LocalDate beginDate, LocalDate endDate) {
    Set<User> userSet = new HashSet<>();
    for(UserSub userSub : subUsers.values()) {
      if(userSub.getUser() != null) {
        if(userSub.getEndDate() == null) {
          if(!userSub.getBeginDate().isAfter(endDate)) {
            userSet.add(userSub.getUser());
          }
        } else {
          if(!userSub.getBeginDate().isAfter(endDate) || !userSub.getEndDate().isBefore(beginDate)) {
            userSet.add(userSub.getUser());
          }
        }
      }
    }
    return userSet;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((number == null) ? 0 : number.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    Subscription other = (Subscription) obj;
    if (number == null) {
      if (other.number != null)
        return false;
    } else if (!number.equals(other.number))
      return false;
    return true;
  }
  
}
