package com.flotta.entity.record;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.flotta.model.registry.Device;
import com.flotta.model.registry.User;


class DeviceTest {

  @Test
  void testConstructor() {
    Device device = new Device("1234567", LocalDate.parse("2020-01-01"));
    assertEquals("1234567", device.getSerialNumber());
    assertEquals(LocalDate.parse("2020-01-01"), device.getCreateDate());
    assertEquals(LocalDate.parse("2020-01-01"), device.getFirstAvailableDate());
  }
  
  @Test
  void testToView() {
    fail("Not yet implemented");
  }

  @Test
  void testToViewLocalDate() {
    fail("Not yet implemented");
  }

  @Test
  void testAddUser() {
    Device device = new Device("1234567", LocalDate.parse("2020-01-01"));
    User user = new User();
    device.addUser(user, LocalDate.parse("2020-01-10"));
    assertEquals(LocalDate.parse("2020-01-10"), device.getFirstAvailableDate());
  }

  @Test
  void testAddSubscription() {
    fail("Not yet implemented");
  }

  @Test
  void testAddNote() {
    fail("Not yet implemented");
  }

  @Test
  void testGetAllModificationDateDesc() {
    fail("Not yet implemented");
  }

}
