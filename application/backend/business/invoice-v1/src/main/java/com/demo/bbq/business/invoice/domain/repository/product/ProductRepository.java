package com.demo.bbq.business.invoice.domain.repository.product;

import com.demo.bbq.business.invoice.domain.repository.product.wrapper.ProductResponseWrapper;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface ProductRepository {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("products/{productCode}")
  Single<ProductResponseWrapper> findByProductCode(@Path(value = "productCode") String productCode);
}

