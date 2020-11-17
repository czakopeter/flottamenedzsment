package com.flotta.utility;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.flotta.entity.switchTable.BasicSwitchTable;

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
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    Assertions.assertNull(Utility.floorDate(map, LocalDate.parse("2020-01-01")));
  }

  @Test
  void floorDateOfDateBeforeAllInMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    map.put(LocalDate.parse("2020-01-10"), null);
    map.put(LocalDate.parse("2020-01-20"), null);
    Assertions.assertNull(Utility.floorDate(map, LocalDate.parse("2020-01-01")));
  }
  
  @Test
  void floorDateOfDateEqualsFirstInMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    map.put(LocalDate.parse("2020-01-10"), null);
    map.put(LocalDate.parse("2020-01-20"), null);
    Assertions.assertEquals(LocalDate.parse("2020-01-10"), Utility.floorDate(map, LocalDate.parse("2020-01-10")));
  }
  
  @Test
  void floorDateOfDateBetweenFirstAndSecondInMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    map.put(LocalDate.parse("2020-01-10"), null);
    map.put(LocalDate.parse("2020-01-20"), null);
    Assertions.assertEquals(LocalDate.parse("2020-01-10"), Utility.floorDate(map, LocalDate.parse("2020-01-15")));
  }
  
  @Test
  void floorDateOfDateAfterLastInMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    map.put(LocalDate.parse("2020-01-10"), null);
    map.put(LocalDate.parse("2020-01-20"), null);
    Assertions.assertEquals(LocalDate.parse("2020-01-20"), Utility.floorDate(map, LocalDate.parse("2020-01-30")));
  }
  
}
