package com.flotta.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.flotta.entity.switchTable.BasicSwitchTable;

class UtilityTest {
  
  @Test
  void getLatestDateOfNull() {
    assertThrows(IllegalArgumentException.class, () -> Utility.getLatestDate(null));
  }
  
  @Test
  void getLatestDateOfEmptyMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    assertThrows(IllegalArgumentException.class, () -> Utility.getLatestDate(map));
  }
  
  @Test
  void getLatestDateOfMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    map.put(LocalDate.parse("2020-01-01"), null);
    map.put(LocalDate.parse("2020-02-01"), null);
    assertThat(Utility.getLatestDate(map)).isEqualTo("2020-02-01");
  }
  
  @Test
  void getfloorDateOfNull() {
    assertThat(Utility.floorDate(null, LocalDate.parse("2020-01-01"))).isNull();
  }
  
  @Test
  void getfloorDateOfEmptyMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    assertThat(Utility.floorDate(map, LocalDate.parse("2020-01-01"))).isNull();
  }

  @Test
  void floorDateOfDateBeforeAllInMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    map.put(LocalDate.parse("2020-01-10"), null);
    map.put(LocalDate.parse("2020-01-20"), null);
    assertThat(Utility.floorDate(map, LocalDate.parse("2020-01-01"))).isNull();
  }
  
  @Test
  void floorDateOfDateEqualsFirstInMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    map.put(LocalDate.parse("2020-01-10"), null);
    map.put(LocalDate.parse("2020-01-20"), null);
    assertThat(Utility.floorDate(map, LocalDate.parse("2020-01-10"))).isEqualTo("2020-01-10");
  }
  
  @Test
  void floorDateOfDateBetweenFirstAndSecondInMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    map.put(LocalDate.parse("2020-01-10"), null);
    map.put(LocalDate.parse("2020-01-20"), null);
    assertThat(Utility.floorDate(map, LocalDate.parse("2020-01-15"))).isEqualTo("2020-01-10");
  }
  
  @Test
  void floorDateOfDateAfterLastInMap() {
    Map<LocalDate, BasicSwitchTable> map = new HashMap<LocalDate, BasicSwitchTable>();
    map.put(LocalDate.parse("2020-01-10"), null);
    map.put(LocalDate.parse("2020-01-20"), null);
    assertThat(Utility.floorDate(map, LocalDate.parse("2020-01-30"))).isEqualTo("2020-01-20");
  }
  
}
