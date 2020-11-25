package com.flotta.utility;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.flotta.model.switchTable.BasicSwitchTable;

class UtilityTest {
  
  @Test
  void getLatestDateOfNull() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Utility.getLatestDate(null));
  }
  
  @Test
  void getLatestDateOfEmptyMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    Assertions.assertThrows(IllegalArgumentException.class, () -> Utility.getLatestDate(map));
  }
  
  @Test
  void getLatestDateOfMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    map.put(LocalDate.parse("2020-01-01"), null);
    map.put(LocalDate.parse("2020-02-01"), null);
    Assertions.assertEquals(LocalDate.parse("2020-02-01"), Utility.getLatestDate(map));
  }
  
  @Test
  void getfloorDateOfNull() {
    Assertions.assertNull(Utility.floorDate(null, LocalDate.parse("2020-01-01")));
  }
  
  @Test
  void getfloorDateOfEmptyMap() {
    List<LocalDate> dates = new LinkedList<>();
    Assertions.assertNull(Utility.floorDate(dates, LocalDate.parse("2020-01-01")));
  }

  @Test
  void floorDateOfDateBeforeAllInMap() {
    List<LocalDate> dates = new LinkedList<>();
    dates.add(LocalDate.parse("2020-01-10"));
    dates.add(LocalDate.parse("2020-01-20"));
    Assertions.assertNull(Utility.floorDate(dates, LocalDate.parse("2020-01-01")));
  }
  
  @Test
  void floorDateOfDateEqualsFirstInMap() {
    List<LocalDate> dates = new LinkedList<>();
    dates.add(LocalDate.parse("2020-01-10"));
    dates.add(LocalDate.parse("2020-01-20"));
    Assertions.assertEquals(LocalDate.parse("2020-01-10"), Utility.floorDate(dates, LocalDate.parse("2020-01-10")));
  }
  
  @Test
  void floorDateOfDateBetweenFirstAndSecondInMap() {
    List<LocalDate> dates = new LinkedList<>();
    dates.add(LocalDate.parse("2020-01-10"));
    dates.add(LocalDate.parse("2020-01-20"));
    Assertions.assertEquals(LocalDate.parse("2020-01-10"), Utility.floorDate(dates, LocalDate.parse("2020-01-15")));
  }
  
  @Test
  void floorDateOfDateAfterLastInMap() {
    List<LocalDate> dates = new LinkedList<>();
    dates.add(LocalDate.parse("2020-01-10"));
    dates.add(LocalDate.parse("2020-01-20"));
    Assertions.assertEquals(LocalDate.parse("2020-01-20"), Utility.floorDate(dates, LocalDate.parse("2020-01-30")));
  }
  
}
