package com.flotta.enums;

public enum MessageKey {
//  LOGIN
  CREATE_FIRST_ADMIN("createFirstAdmin"),
  SUCCESSFUL_REGISTRATION("successfulRegistration"),
  ACTIVATE_OR_CREATE_FIRST_ADMIN("activateOrCreateFirstAdmin"),
  ALREADY_HAS_ADMIN("alreadyHasAdmin"),
  SUCCESSFUL_ACTIVATION("successfulActivation"),
  UNKNOWN_ACTIVATION_KEY("unknownActivationKey"),
  
  LOGIN_FAILURE("loginFailure"),
  SUCCESSFUL_LOGOUT("successfulLogout"),
  
//  USER
  EMAIL_ALREADY_USED("emailAlreadyUsed"),
  UNKNOWN_USER("unknownUser"),
  NO_REDUCE_ADMIN("noReduceAdmin"),
  
//  SUBSCRIPTIION
  NO_FREE_SIM("noFreeSim"),
  PHONE_NUMBER_ALREADY_USED("phoneNumberAlreadyUsed"),
  PHONE_NUMBER_INVALID("phoneNumberInvalid"),
  UNKNOWN_SUBSCRIPITON("unknownSubscription"),
  
//  SIM CARD
  IMEI_ALREADY_USED("imeiAlreadyUsed"),
  IMEI_NUMBER_INVALID("imeiNumberInvalid"),
  
//  DEVICE
  NO_DEVICE_TYPE("noDeviceType"),
  SERIAL_NUMBER_ALREADY_USED("serialNumberAlreadyUsed"),
  UNKNOWN_DEVICE("unknownDevice"),
  
  
//  DEVICE TYPE
  NAME_ALREADY_USED("nameAlreadyExists"),
  BRAND_AND_MODEL_ALREADY_USED("brandAndModelAlreadyExists"),
  UNKNOWN_DEVICE_TYPE("unknownDeviceType"),
  
//  INVOICE
  INVOICE_NUMBER_ALREADY_USED("invoiceNumberAlreadyUsed"),
  UNKNOWN_INVOICE("unknownInvoice"),
  
//  INVOICE CONFIG
  UNKNOWN_COUPLER("unknownCoupler"),
  COUPLER_NAME_ALREADY_USED("couplerNameAlreadyUsed"),
  
  UNKNOWN_CHARGE_RATIO("unknownChargeRatio"),
  CHARGE_RATIO_NAME_ALREADY_USED("chargeRatioNameAlreadyUsed"),
  
  UNKNOWN_PARTICIPANT("unknownParticipant"),
  PARTICIPANT_NAME_ALREADY_USED("paricipantNameAlreadyUsed"),
  
//  EMAIL
  
//  PROFILE
  PASSWORD_CHANGE_SUCCESSFUL("passwordChangeSuccesful"),
  CURRENT_PASSWORD_INCORRECT("currentPasswordIncorrect"),
  NEW_PASSWORD_INVALID("newPasswordInvalid"),
  PASSWORD_NEW_OLD_SAME("passwordNewOldSame"),
  
  EMAIL_FAILURE("emailFailure"),
  UNKNOWN_EMAIL("unknownEmail"),
  
  PAGE_NOT_FOUND("pageNotFound"), 
  UNKNOWN_FAILURE("unknownFailure")
  
  ;
  
  
  private String key;
  
  private MessageKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
