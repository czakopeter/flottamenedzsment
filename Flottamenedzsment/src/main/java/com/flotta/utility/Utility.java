package com.flotta.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.flotta.model.BasicEntity;
import com.flotta.model.registry.User;
import com.flotta.model.switchTable.BasicSwitchTable;
import com.flotta.model.switchTable.UserDev;

public class Utility {
  
  public static LocalDate getLatestDate(Map<LocalDate, ? extends BasicSwitchTable> map) {
    if(map == null || map.isEmpty()) {
      throw new IllegalArgumentException("Map should contains at least one element");
    }
    List<LocalDate> dates = new LinkedList<>(map.keySet());
    Collections.sort(dates, Collections.reverseOrder());
    return dates.get(0);
  }
  
  public static LocalDate floorDate(List<LocalDate> dates, LocalDate date) {
    if (dates == null || dates.isEmpty() || date == null) {
      return null;
    }
    if (dates.contains(date)) {
      return date;
    }
    Collections.sort(dates);
    LocalDate r = null;
    for (LocalDate act : dates) {
      if (date.isBefore(act)) {
        break;
      }
      r = act;
    }
    return r;
  }
  
  public static LocalDate ceilDate(List<LocalDate> dates, LocalDate date) {
    if (dates == null || dates.isEmpty() || date == null) {
      return null;
    }
    if (dates.contains(date)) {
      return date;
    }
    Collections.sort(dates, Collections.reverseOrder());
    LocalDate r = null;
    for (LocalDate act : dates) {
      if (date.isAfter(act)) {
        break;
      }
      r = act;
    }
    return r;
  }
  
  public static boolean areTwoLastAreSame(Map<LocalDate, ? extends BasicSwitchTable> map) {
    if(map == null) {
      throw new NullPointerException();
    }
    if(map.size() > 1) {
      List<LocalDate> dates = new LinkedList<>(map.keySet());
      Collections.sort(dates, Collections.reverseOrder());
      return equals(map.get(dates.get(0)), map.get(dates.get(1)));
    }
    return false;
  }
  
  public static boolean equals(BasicSwitchTable o1, BasicSwitchTable o2) {
    if(o1 == null || o2 == null) {
      throw new NullPointerException();
    }
    if(o1.getClass() != o2.getClass()) {
      throw new IllegalArgumentException();
    }
    
    return o1.isSameSwitchedPairs(o2);
  }
  
  public static <E extends BasicEntity> boolean isSameByIdOrBothNull(E e1, E e2) {
    if(e1 == null && e2 == null) {
      return true;
    }
    if(e1 == null || e2 == null) {
      return false;
    }
    return e1.equals(e2);
  }
  
//  https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
  public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
  public static String getPeriod(LocalDate beginDate, LocalDate endDate) {
    StringBuilder sb = new StringBuilder();
    if(beginDate == null || (endDate != null && beginDate.isAfter(endDate))) {
    } else {
      sb.append(beginDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
      sb.append(" - ");
      if(endDate != null) {
        if(beginDate.getYear() < endDate.getYear()) {
          sb.append(endDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        } else {
          sb.append(endDate.format(DateTimeFormatter.ofPattern("MM.dd")));
        }
      }
    }
    return sb.toString();
  }
  
  public static BasicSwitchTable getBasicSwitchTable(Map<LocalDate, ? extends BasicSwitchTable> map) {
    if(map == null || map.isEmpty()) {
      return null;
    }
    List<LocalDate> beginDates = new LinkedList<>(map.keySet());
    Collections.sort(beginDates, Collections.reverseOrder());
    LocalDate lastBeginDate = beginDates.get(0);
    BasicSwitchTable last = map.get(lastBeginDate);
    return last.getEndDate() == null ? last : null;
  }
  
  public static BasicSwitchTable getBasicSwitchTable(Map<LocalDate, ? extends BasicSwitchTable> map, LocalDate ceil) {
    if(map == null || map.isEmpty()) {
      return null;
    }
    if(map.containsKey(ceil)) {
      return map.get(ceil);
    }
    
    List<LocalDate> beginDates = new LinkedList<>(map.keySet());
    Collections.sort(beginDates, Collections.reverseOrder());
    LocalDate floorBeginDate = null;
    for(LocalDate date : beginDates) {
      if(ceil.isAfter(date)) {
        floorBeginDate = date;
        break;
      }
    }
    if(floorBeginDate == null) {
      return null;
    }
    BasicSwitchTable last = map.get(floorBeginDate);
    if(last.getEndDate() == null) {
      return last;
    } else {
      return last.getEndDate().isBefore(ceil) ? null : last;
    }
    
  }
}
