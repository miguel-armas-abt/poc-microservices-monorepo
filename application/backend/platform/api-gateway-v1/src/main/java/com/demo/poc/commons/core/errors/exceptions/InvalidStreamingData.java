package com.demo.poc.commons.core.errors.exceptions;

import com.demo.poc.commons.custom.exceptions.ErrorDictionary;
import lombok.Getter;

@Getter
public class InvalidStreamingData extends GenericException {

  public InvalidStreamingData(String message) {
    super(message, ErrorDictionary.parse(InvalidStreamingData.class));
  }
}
