package com.flotta.enums;

public enum MessageKey {
  SUCCESSFULL_CREATION("succesfulCreation"),
  EMAIL_FAILURE("emailFailure"),
  NO_REDUCE_ADMIN("noReduceAdmin"),
  PASSWORD_CHANGE_SUCCESSFUL("passwordChangeSuccesful"),
  OLD_PASSWORD_INCORRECT("oldPasswordIncorrect"),
  NEW_PASSWORD_INVALID("newPasswordInvalid"),
  PASSWORD_NEW_OLD_SAME("passwordNewOldSame"),
  UNKNOWN_EMAIL("unknownEmail"), 
  SUCCESSFUL_ACTIVATION("successfulActivation"), 
  UNKNOWN_ACTIVATION_KEY("unknownActivationKey"),
  ALREADY_HAS_ADMIN("alreadyHasAdmin"),
  SUCCESSFUL_REGISTRATION("successfulRegistration"), 
  UNKNOWN_INVOICE("unknownInvoice"),
  LOGIN_FAILURE("loginFailure"),
  SUCCESSFUL_LOGOUT("successfulLogout"), 
  ACTIVATE_OR_CREATE_FIRST_ADMIN("activateOrCreateFirstAdmin"), 
  CREATE_FIRST_ADMIN("createFirstAdmin"),
  EMAIL_ALREADY_USED("emailAlreadyUsed"),
  UNKNOWN_USER("unknownUser"),
  IMEI_ALREADY_USED("imeiAlreadyUsed"),
  IMEI_NUMBER_NOT_VALID("imeiNumberNotValid"),
  NO_FREE_SIM("noFreeSim"),
  PHONE_NUMBER_ALREADY_USED("phoneNumberAlreadyUsed"),
  PHONE_NUMBER_INVALID("phoneNumberInvalid"),
  UNKNOWN_SUBSCRIPITON("unknownSubscription"),
  SIM_CHANGE_REASON_EMPTY("simChangeReasonEmpty"),
  SERIAL_NUMBER_ALREADY_USED("serialNumberAlreadyUsed"),
  UNKNOWN_DEVICE("unknownDevice"),
  NO_DEVICE_TYPE("noDeviceType"),
  NAME_ALREADY_EXISTS("nameAlreadyExists"),
  BRAND_AND_MODEL_ALREADY_EXISTS("brandAndModelAlreadyExists"),
  UNKNOWN_DEVICE_TYPE("unknownDeviceType");
  
  
  private String key;
  
  private MessageKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
