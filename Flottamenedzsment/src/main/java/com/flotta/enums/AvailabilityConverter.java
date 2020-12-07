package com.flotta.enums;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AvailabilityConverter implements AttributeConverter<Availability, Integer> {

  @Override
  public Integer convertToDatabaseColumn(Availability status) {
    if (status == null) {
      return null;
    }
    return status.getCode();
  }

  @Override
  public Availability convertToEntityAttribute(Integer code) {
    if (code == null) {
      return null;
    }

    return Stream.of(Availability.values()).filter(c -> c.getCode() == code).
        findFirst().
        orElseThrow(IllegalArgumentException::new);
  }

}
