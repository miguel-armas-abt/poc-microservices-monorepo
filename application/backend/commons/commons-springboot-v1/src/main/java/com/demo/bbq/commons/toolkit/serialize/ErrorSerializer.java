package com.demo.bbq.commons.toolkit.serialize;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.exceptions.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.HttpStatusCodeException;

@RequiredArgsConstructor
public class ErrorSerializer {

  private final JsonSerializer serializer;

  public <T> T toObject(Throwable cause, Class<T> objectClass) {
    if((cause instanceof ExternalServiceException exception) && objectClass.isAssignableFrom(ErrorDTO.class)) {
      return (T) exception.getErrorDetail();
    }

    if(cause instanceof HttpStatusCodeException exception) {
      String jsonBody = exception.getResponseBodyAsString();
      return serializer.readNullableObject(jsonBody, objectClass);
    }

    propagate(cause);
    return null;
  }

  @SuppressWarnings("unchecked")
  private static <E extends Throwable> void propagate(Throwable exception) throws E {
    throw (E) exception;
  }
}
