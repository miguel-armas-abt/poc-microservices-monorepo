package com.demo.bbq.infrastructure.apigateway.domain.repository.config;

import com.demo.bbq.infrastructure.apigateway.domain.exception.ApiGatewayException;
import com.demo.bbq.support.httpclient.retrofit.util.RetrofitInterceptorUtil;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
public class ApiExceptionInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) {
    return RetrofitInterceptorUtil.validateErrorResponse.apply(chain, ApiGatewayException.ERROR0003.buildException());
  }

}
