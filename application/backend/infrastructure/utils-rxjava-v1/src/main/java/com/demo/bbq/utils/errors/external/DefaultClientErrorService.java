package com.demo.bbq.utils.errors.external;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.serializer.ErrorSerializerUtil;
import java.util.Optional;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.tuple.Pair;

public class DefaultClientErrorService implements RestClientErrorService<ErrorDTO> {

  @Override
  public Optional<Pair<String, String>> getCodeAndMessage(ResponseBody errorBody) {
    ErrorDTO error = ErrorSerializerUtil.deserializeError(errorBody, ErrorDTO.class);
    return Optional.ofNullable(error)
        .map(errorNullable -> Pair.of(errorNullable.getCode(), errorNullable.getMessage()));
  }

  @Override
  public boolean supports(Class<?> wrapperClass) {
    return wrapperClass.isAssignableFrom(ErrorDTO.class);
  }
}
