package com.demo.poc.commons.core.restclient.error;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.dto.ErrorType;
import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
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
public class RestClientErrorHandler {

  public static Pair<ErrorDto, HttpStatus> build(Response response,
                                                 List<RestClientErrorExtractor> serviceList,
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
            throw new JsonReadException(ex.getMessage());
          }

          Optional<Pair<String, String>> optionalPair = serviceError.getCodeAndMessage(jsonBody);
          optionalPair.ifPresent(currentPair -> {
            atomicPair.set(currentPair);
            firstMatch.set(Boolean.TRUE);
          });
        });

    return Optional.ofNullable(atomicPair.get())
        .map(pairCodeAndMessage -> Pair.of(ErrorDto.builder()
            .type(ErrorType.EXTERNAL)
            .code(pairCodeAndMessage.getLeft())
            .message(pairCodeAndMessage.getRight())
            .build(), HttpStatus.valueOf(response.code())))
        .orElseGet(() -> {
          ErrorDto error = ErrorDto.getDefaultError(properties);
          error.setType(ErrorType.EXTERNAL);
          return Pair.of(error, HttpStatus.BAD_REQUEST);
        });
  }
}
