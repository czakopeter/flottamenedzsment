package com.flotta.enums;

public enum MessageKey {
  NO_DEVICE_TYPE("noDeviceType"),
  ALREADY_EXISTS("alreadyExists"),
  NOT_EXISTS("notExists"),
  SUCCESSFULL_CREATION("succesfulCreation"),
  NO_SIM("noSim"),
  NAME_ALREADY_EXISTS("nameAlreadyExists"),
  BRAND_AND_MODEL_ALREADY_EXISTS("brandAndModelAlreadyExists"),
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
  UNKNOWN_INVOICE("unknownInvoice");
  
  
  private String key;
  
  private MessageKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
