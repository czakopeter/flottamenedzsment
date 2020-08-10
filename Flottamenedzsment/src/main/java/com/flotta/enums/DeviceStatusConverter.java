package com.flotta.enums;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DeviceStatusConverter implements AttributeConverter<DeviceStatusEnum, Integer> {

  @Override
  public Integer convertToDatabaseColumn(DeviceStatusEnum status) {
    if (status == null) {
      return null;
    }
    return status.getCode();
  }

  @Override
  public DeviceStatusEnum convertToEntityAttribute(Integer code) {
    if (code == null) {
      return null;
    }

    return Stream.of(DeviceStatusEnum.values()).filter(c -> c.getCode() == code).
        findFirst().
        orElseThrow(IllegalArgumentException::new);
  }

}
