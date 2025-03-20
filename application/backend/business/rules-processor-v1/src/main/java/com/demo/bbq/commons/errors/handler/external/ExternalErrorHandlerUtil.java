package com.demo.bbq.commons.errors.handler.external;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.dto.ErrorType;
import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import com.demo.bbq.commons.properties.base.ConfigurationBaseProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExternalErrorHandlerUtil {

  public static Pair<ErrorDTO, HttpStatus> build(Response response,
                                                 List<RestClientErrorStrategy> serviceList,
                                                 ConfigurationBaseProperties properties) {

    AtomicReference<Pair<String, String>> atomicPair = new AtomicReference<>();
    AtomicReference<Boolean> firstMatch = new AtomicReference<>(Boolean.FALSE);

    serviceList
        .forEach(serviceError -> {
          if (firstMatch.get())
            return;

          String jsonBody;
          try{
            jsonBody = response.errorBody().string();
          } catch (IOException ex) {
            throw new SystemException("DeserializeJSONError", ex.getMessage());
          }

          Optional<Pair<String, String>> optionalPair = serviceError.getCodeAndMessage(jsonBody);
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
