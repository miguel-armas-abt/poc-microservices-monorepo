package com.demo.bbq.support.httpclient.retrofit.util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.function.BiFunction;

import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDto;
import com.google.gson.Gson;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.HttpStatus;

public class RetrofitInterceptorUtil {
  private RetrofitInterceptorUtil() {}

  private static final BiFunction<ApiExceptionDto, Integer, ApiException> mapToApiException = (apiExceptionDto, httpCode) ->
      ApiException.builder()
          .type(apiExceptionDto.getType())
          .message(apiExceptionDto.getMessage())
          .errorCode(apiExceptionDto.getErrorCode())
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
        ApiExceptionDto exceptionResponse = new Gson().fromJson(jsonExceptionResponse, ApiExceptionDto.class);
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
