package com.demo.bbq.support.httpclient.retrofit.util;

import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDTO;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.function.BiFunction;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.HttpStatus;

public class RetrofitInterceptorUtil {
  private RetrofitInterceptorUtil() {}

  private static final BiFunction<ApiExceptionDTO, Integer, ApiException> mapToApiException = (apiExceptionDTO, httpCode) ->
      ApiException.builder()
          .type(apiExceptionDTO.getType())
          .message(apiExceptionDTO.getMessage())
          .errorCode(apiExceptionDTO.getErrorCode())
          .status(HttpStatus.resolve(httpCode))
          .build();

  public static BiFunction<Interceptor.Chain, ApiException, Response> validateErrorResponse = (chain, timeoutApiException) -> {
    try {
      Response response = chain.proceed(chain.request());
      if (!response.isSuccessful()) {
        if (response.code() == 503) {
          throw timeoutApiException;
        }
        ResponseBody responseBody = response.body();
        String jsonExceptionResponse = responseBody != null ? responseBody.string() : "";
        ApiExceptionDTO exceptionResponse = new Gson().fromJson(jsonExceptionResponse, ApiExceptionDTO.class);
        throw mapToApiException.apply(exceptionResponse, response.code());
      }
      return response;
    } catch (IOException exception) {
      if (exception instanceof SocketTimeoutException) {
        throw timeoutApiException;
      }
      throw new RuntimeException(exception);
    }
  };

}
