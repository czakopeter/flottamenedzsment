package com.flotta.enums;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SimStatusConverter implements AttributeConverter<SimStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(SimStatus status) {
    if (status == null) {
      return null;
    }
    return status.getCode();
  }

  @Override
  public SimStatus convertToEntityAttribute(Integer code) {
    if (code == null) {
      return null;
    }

    return Stream.of(SimStatus.values()).filter(c -> c.getCode() == code).
        findFirst().
        orElseThrow(IllegalArgumentException::new);
  }

}
