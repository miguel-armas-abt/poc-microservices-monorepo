package com.demo.bbq.business.invoice.domain.repository.restclient.product;

import com.demo.bbq.business.invoice.domain.repository.restclient.product.response.ProductResponseWrapper;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface ProductApi {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("products/{productCode}")
  Single<ProductResponseWrapper> findByProductCode(@Path(value = "productCode") String productCode);
}

