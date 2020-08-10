package com.flotta.enums;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SubscriptionStatusConverter implements AttributeConverter<SubscriptionStatusEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(SubscriptionStatusEnum status) {
    if (status == null) {
      return null;
    }
    return status.getCode();
  }

  @Override
  public SubscriptionStatusEnum convertToEntityAttribute(Integer code) {
    if (code == null) {
      return null;
    }

    return Stream.of(SubscriptionStatusEnum.values()).filter(c -> c.getCode() == code).
        findFirst().
        orElseThrow(IllegalArgumentException::new);
  }

}
