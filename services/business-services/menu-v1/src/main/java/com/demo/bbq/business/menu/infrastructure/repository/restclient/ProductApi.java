package com.demo.bbq.business.menu.infrastructure.repository.restclient;

import com.demo.bbq.business.menu.infrastructure.repository.restclient.dto.ProductDto;
import com.demo.bbq.business.menu.infrastructure.repository.restclient.dto.ProductRequestDto;
import io.reactivex.Single;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface ProductApi {

  @Headers({"Accept: application/json"})
  @GET("products/{id}")
  Single<ProductDto> findById(@Path(value = "id") Long id);

  @Headers({"Accept: application/json"})
  @GET("products")
  Single<List<ProductDto>> findByScope(@Query("scope") String scope);

  @POST("products")
  Single<ResponseBody> save(@Body ProductRequestDto productRequest);

  @PUT("products/{id}")
  Single<ResponseBody> update(@Path(value = "id") Long id, @Body ProductRequestDto productRequest);

  @DELETE("products/{id}")
  Single<Response<Void>> delete(@Path(value = "id") Long id);
}
