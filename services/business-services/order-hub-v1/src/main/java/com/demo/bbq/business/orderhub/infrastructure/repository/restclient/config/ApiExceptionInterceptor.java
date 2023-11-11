package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.config;

import com.demo.bbq.business.orderhub.domain.exception.OrderHubException;
import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDto;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.SocketTimeoutException;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
@Component
public class ApiExceptionInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    try {
      Response response = chain.proceed(chain.request());
      if (!response.isSuccessful()) {
        if (response.code() == 503) {
          throw OrderHubException.ERROR0002.buildException();
        }
        ResponseBody responseBody = response.body();
        String jsonExceptionResponse = responseBody != null ? responseBody.string() : "";
        ApiExceptionDto exceptionResponse = new Gson().fromJson(jsonExceptionResponse, ApiExceptionDto.class);
        throw mapToApiException(exceptionResponse, response.code());
      }
      return response;
    } catch (SocketTimeoutException exception) {
      throw OrderHubException.ERROR0002.buildException();
    }
  }

  private ApiException mapToApiException(ApiExceptionDto exception, int httpCode) {
    return ApiException.builder()
        .type(exception.getType())
        .message(exception.getMessage())
        .errorCode(exception.getErrorCode())
        .status(HttpStatus.resolve(httpCode))
        .build();
  }
}
