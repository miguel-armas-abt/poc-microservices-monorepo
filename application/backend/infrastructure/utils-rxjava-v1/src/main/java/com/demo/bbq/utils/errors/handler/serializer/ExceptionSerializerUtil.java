package com.demo.bbq.utils.errors.handler.serializer;

import com.demo.bbq.utils.errors.dto.ErrorDTO;
import com.demo.bbq.utils.errors.dto.ErrorType;
import com.demo.bbq.utils.errors.handler.external.service.RestClientErrorService;
import com.demo.bbq.utils.errors.handler.matcher.ErrorMatcherUtil;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import retrofit2.Response;

public class ExceptionSerializerUtil {

  private ExceptionSerializerUtil() {}

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final Gson gson = new Gson();

  public static <T> byte[] serializeObject(T object, String defaultMessage) {
    try {
      return objectMapper.writeValueAsBytes(object);
    } catch (JsonProcessingException e) {
      return defaultMessage.getBytes(StandardCharsets.UTF_8);
    }
  }

  public static <T> T deserializeError(ResponseBody responseBody, Class<T> objectClass) {
    try {
      String stringJson = responseBody.string();
      return gson.fromJson(stringJson, objectClass);
    } catch (IOException ex) {
      return null;
    }
  }

  public static Pair<ErrorDTO, HttpStatus> matchExternalError(Response response,
                                                              List<RestClientErrorService> serviceList,
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
          ErrorDTO error = ErrorMatcherUtil.getDefaultError(properties);
          error.setType(ErrorType.EXTERNAL);
          return Pair.of(error, HttpStatus.BAD_REQUEST);
        });
  }

  @SuppressWarnings("unchecked")
  public static <E extends Throwable> void propagate(Throwable exception) throws E {
    throw (E) exception;
  }
}
