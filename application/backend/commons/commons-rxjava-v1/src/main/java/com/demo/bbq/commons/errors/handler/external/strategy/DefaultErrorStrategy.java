package com.demo.bbq.commons.errors.handler.external.strategy;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.serializer.ErrorSerializerUtil;
import java.util.Optional;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.tuple.Pair;

public class DefaultErrorStrategy implements RestClientErrorStrategy {

  @Override
  public Optional<Pair<String, String>> getCodeAndMessage(ResponseBody errorBody) {
    ErrorDTO error = ErrorSerializerUtil.deserializeError(errorBody, ErrorDTO.class);
    return Optional.ofNullable(error)
        .map(errorNullable -> Pair.of(errorNullable.getCode(), errorNullable.getMessage()));
  }

  @Override
  public boolean supports(Class<? extends ExternalErrorWrapper> errorWrapperClass) {
    return errorWrapperClass.isAssignableFrom(ErrorDTO.class);
  }
}
