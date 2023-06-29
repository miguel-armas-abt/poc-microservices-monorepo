package com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.config;

import com.demo.bbq.support.exception.model.ApiException;
import com.demo.bbq.support.exception.model.ApiExceptionResponse;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
@Component
public class ApiExceptionInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    if (!response.isSuccessful()) {
      ResponseBody responseBody = response.body();
      String jsonExceptionResponse = responseBody != null ? responseBody.string() : "";
      ApiExceptionResponse exceptionResponse = new Gson().fromJson(jsonExceptionResponse, ApiExceptionResponse.class);
      throw mapToApiException(exceptionResponse, response.code());
    }
    return response;
  }

  private ApiException mapToApiException(ApiExceptionResponse exceptionResponse, int httpCode) {
    return ApiException.builder(exceptionResponse.getType(), exceptionResponse.getErrorCode(), exceptionResponse.getTitle(), HttpStatus.resolve(httpCode)).build();
  }
}
