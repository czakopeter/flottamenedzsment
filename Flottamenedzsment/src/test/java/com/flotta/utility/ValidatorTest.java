package com.flotta.utility;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValidatorTest {

  @Test
  void noPhoneNumber() {
    assertFalse(Validator.validHunPhoneNumber(""));
  }
  
  @Test
  void validPhoneNumber() {
    assertTrue(Validator.validHunPhoneNumber("201234567"));
  }
  
  @Test
  void invalidPhoneNumber() {
    assertFalse(Validator.validHunPhoneNumber("2012345678"));
  }
  
  @Test
  void validImieWithLuhnAlg() {
    assertTrue(Validator.isValidImieWithLuhnAlg("8936304419070454006"));
  }
  
  @Test
  void invalidImieWithLuhnAlg() {
    assertFalse(Validator.isValidImieWithLuhnAlg("8936304419070454007"));
  }

}
