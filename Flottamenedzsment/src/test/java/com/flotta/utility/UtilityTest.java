package com.flotta.utility;


import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.flotta.model.switchTable.BasicSwitchTable;

class UtilityTest {
  
  @Test
  void getLatestDateOfNull() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Utility.getLatestSwitchTableDate(null));
  }
  
  @Test
  void getLatestDateOfEmptyMap() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Utility.getLatestSwitchTableDate(Collections.emptyMap()));
  }
  
  @Test
  void getLatestDateOfMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    map.put(LocalDate.parse("2020-01-01"), null);
    map.put(LocalDate.parse("2020-02-01"), null);
    Assertions.assertEquals(LocalDate.parse("2020-02-01"), Utility.getLatestSwitchTableDate(map));
  }
  
}
