package com.flotta.enums;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SimStatusConverter implements AttributeConverter<SimStatusEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(SimStatusEnum status) {
    if (status == null) {
      return null;
    }
    return status.getCode();
  }

  @Override
  public SimStatusEnum convertToEntityAttribute(Integer code) {
    if (code == null) {
      return null;
    }

    return Stream.of(SimStatusEnum.values()).filter(c -> c.getCode() == code).
        findFirst().
        orElseThrow(IllegalArgumentException::new);
  }

}
