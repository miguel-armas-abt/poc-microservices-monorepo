package com.demo.bbq.repository.menu.menuv2;

import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface MenuV2Repository {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("menu-options/{productCode}")
  Single<MenuOptionResponseWrapper> findByProductCode(@Path(value = "productCode") String productCode);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("menu-options")
  Observable<ResponseBody> findByCategory(@Query("category") String category);

  @Streaming
  @POST("menu-options")
  Single<ResponseBody> save(@Body MenuOptionSaveRequestWrapper menuOptionRequest);

  @Streaming
  @PUT("menu-options/{productCode}")
  Single<ResponseBody> update(@Path(value = "productCode") String productCode, @Body MenuOptionUpdateRequestWrapper menuOptionRequest);

  @Streaming
  @DELETE("menu-options/{productCode}")
  Single<Response<Void>> delete(@Path(value = "productCode") String productCode);
}
