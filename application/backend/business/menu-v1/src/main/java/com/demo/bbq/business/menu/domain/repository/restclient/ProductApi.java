package com.demo.bbq.business.menu.domain.repository.restclient;

import com.demo.bbq.business.menu.domain.repository.restclient.wrapper.ProductSaveRequestWrapper;
import com.demo.bbq.business.menu.domain.repository.restclient.wrapper.ProductUpdateRequestWrapper;
import com.demo.bbq.business.menu.domain.repository.restclient.wrapper.ProductWrapper;
import io.reactivex.Single;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface ProductApi {

  @Headers({"Accept: application/json"})
  @GET("products/{code}")
  Single<ProductWrapper> findByCode(@Path(value = "code") String code);

  @Headers({"Accept: application/json"})
  @GET("products")
  Single<List<ProductWrapper>> findByScope(@Query("scope") String scope);

  @POST("products")
  Single<ResponseBody> save(@Body ProductSaveRequestWrapper productRequest);

  @PUT("products/{code}")
  Single<ResponseBody> update(@Path(value = "code") String code, @Body ProductUpdateRequestWrapper productRequest);

  @DELETE("products/{code}")
  Single<Response<Void>> delete(@Path(value = "code") String code);
}
