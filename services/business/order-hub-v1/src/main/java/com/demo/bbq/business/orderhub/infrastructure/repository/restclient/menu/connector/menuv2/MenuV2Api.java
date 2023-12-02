package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.connector.menuv2;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionSaveRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionUpdateRequestDto;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface MenuV2Api {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("menu-options/{productCode}")
  Single<MenuOptionDto> findByProductCode(@Path(value = "productCode") String productCode);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("menu-options")
  Observable<ResponseBody> findByCategory(@Query("category") String category);

  @Streaming
  @POST("menu-options")
  Single<ResponseBody> save(@Body MenuOptionSaveRequestDto menuOptionRequest);

  @Streaming
  @PUT("menu-options/{productCode}")
  Single<ResponseBody> update(@Path(value = "productCode") String productCode, @Body MenuOptionUpdateRequestDto menuOptionRequest);

  @Streaming
  @DELETE("menu-options/{productCode}")
  Single<Response<Void>> delete(@Path(value = "productCode") String productCode);
}
