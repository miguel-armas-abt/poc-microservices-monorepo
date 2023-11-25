package com.demo.bbq.business.invoice.infrastructure.repository.restclient;

import com.demo.bbq.business.invoice.infrastructure.repository.restclient.dto.CoreProductDto;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface CoreProductApi {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("products/{productCode}")
  Single<CoreProductDto> findByProductCode(@Path(value = "productCode") String productCode);
}

