package com.demo.bbq.business.invoice.infrastructure.exception;

import com.demo.bbq.business.invoice.domain.exception.InvoiceException;
import com.demo.bbq.support.httpclient.retrofit.util.RetrofitInterceptorUtil;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
public class RestClientExceptionInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) {
    return RetrofitInterceptorUtil.validateErrorResponse.apply(chain, InvoiceException.ERROR0002.buildException());
  }

}
