package com.demo.bbq.utils.errors.handler.external;

import com.demo.bbq.utils.errors.handler.serializer.ExceptionSerializerUtil;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Response;

@Slf4j
public class ExternalClientErrorUtil {

  private ExternalClientErrorUtil() {}

  public static Response handleError(Interceptor.Chain chain,
                                     ConfigurationBaseProperties properties) {
    Response response = null;
    try {
      response = chain.proceed(chain.request());
      if(!response.isSuccessful() && response.body() != null) {}
    } catch (IOException exception) {
      ExceptionSerializerUtil.propagate(exception);
    }
    return response;
  }
}