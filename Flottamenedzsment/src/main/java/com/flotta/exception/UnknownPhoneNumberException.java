package com.flotta.exception;

public class UnknownPhoneNumberException extends Exception {

  public UnknownPhoneNumberException(String number) {
    super(number);
  }
  
}
