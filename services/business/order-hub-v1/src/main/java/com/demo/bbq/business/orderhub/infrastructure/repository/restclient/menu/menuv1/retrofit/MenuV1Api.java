package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.menuv1.retrofit;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionSaveRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionUpdateRequestDto;
import io.reactivex.Single;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface MenuV1Api {

  @Headers({"Accept: application/json"})
  @GET("menu-options/{productCode}")
  Single<MenuOptionDto> findByProductCode(@Path(value = "productCode") String productCode);

  @Headers({"Accept: application/json"})
  @GET("menu-options")
  Single<List<MenuOptionDto>> findByCategory(@Query("category") String category);

  @POST("menu-options")
  Single<ResponseBody> save(@Body MenuOptionSaveRequestDto menuOptionRequest);

  @PUT("menu-options/{productCode}")
  Single<ResponseBody> update(@Path(value = "productCode") String productCode, @Body MenuOptionUpdateRequestDto menuOptionRequest);

  @DELETE("menu-options/{productCode}")
  Single<Response<Void>> delete(@Path(value = "productCode") String productCode);
}
