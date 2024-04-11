package com.demo.bbq.infrastructure.authadapter.infrastructure.exception;

import com.demo.bbq.infrastructure.authadapter.domain.exception.AuthAdapterException;
import com.demo.bbq.support.httpclient.retrofit.util.RetrofitInterceptorUtil;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
public class RestClientExceptionInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) {
    return RetrofitInterceptorUtil.validateErrorResponse.apply(chain, AuthAdapterException.ERROR0004.buildException());
  }

}
