package com.flotta.enums;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatusEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(UserStatusEnum status) {
    if (status == null) {
      return null;
    }
    return status.getCode();
  }

  @Override
  public UserStatusEnum convertToEntityAttribute(Integer code) {
    if (code == null) {
      return null;
    }

    return Stream.of(UserStatusEnum.values()).filter(c -> c.getCode() == code).
        findFirst().
        orElseThrow(IllegalArgumentException::new);
  }

}
