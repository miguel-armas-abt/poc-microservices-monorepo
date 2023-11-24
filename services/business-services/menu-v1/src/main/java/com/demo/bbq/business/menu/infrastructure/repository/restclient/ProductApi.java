package com.demo.bbq.business.menu.infrastructure.repository.restclient;

import com.demo.bbq.business.menu.infrastructure.repository.restclient.dto.ProductDto;
import com.demo.bbq.business.menu.infrastructure.repository.restclient.dto.ProductSaveRequestDto;
import io.reactivex.Single;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface ProductApi {

  @Headers({"Accept: application/json"})
  @GET("products/{id}")
  Single<ProductDto> findByCode(@Path(value = "code") String code);

  @Headers({"Accept: application/json"})
  @GET("products")
  Single<List<ProductDto>> findByScope(@Query("scope") String scope);

  @POST("products")
  Single<ResponseBody> save(@Body ProductSaveRequestDto productRequest);

  @PUT("products/{code}")
  Single<ResponseBody> update(@Path(value = "code") String code, @Body ProductSaveRequestDto productRequest);

  @DELETE("products/{code}")
  Single<Response<Void>> delete(@Path(value = "code") String code);
}
