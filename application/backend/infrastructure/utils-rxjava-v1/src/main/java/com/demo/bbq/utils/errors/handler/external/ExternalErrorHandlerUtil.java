package com.demo.bbq.utils.errors.handler.external;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.dto.ErrorType;
import com.demo.bbq.utils.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import retrofit2.Response;

public class ExternalErrorHandlerUtil {

  private ExternalErrorHandlerUtil() {}

  public static Pair<ErrorDTO, HttpStatus> build(Response response,
                                                 List<RestClientErrorStrategy> serviceList,
                                                 ConfigurationBaseProperties properties) {

    AtomicReference<Pair<String, String>> atomicPair = new AtomicReference<>();
    AtomicReference<Boolean> firstMatch = new AtomicReference<>(Boolean.FALSE);

    serviceList
        .forEach(serviceError -> {
          if (firstMatch.get())
            return;

          Optional<Pair<String, String>> optionalPair = serviceError.getCodeAndMessage(response.errorBody());
          optionalPair.ifPresent(currentPair -> {
            atomicPair.set(currentPair);
            firstMatch.set(Boolean.TRUE);
          });
        });

    return Optional.ofNullable(atomicPair.get())
        .map(pairCodeAndMessage -> Pair.of(ErrorDTO.builder()
            .type(ErrorType.EXTERNAL)
            .code(pairCodeAndMessage.getLeft())
            .message(pairCodeAndMessage.getRight())
            .build(), HttpStatus.valueOf(response.code())))
        .orElseGet(() -> {
          ErrorDTO error = ErrorDTO.getDefaultError(properties);
          error.setType(ErrorType.EXTERNAL);
          return Pair.of(error, HttpStatus.BAD_REQUEST);
        });
  }
}
