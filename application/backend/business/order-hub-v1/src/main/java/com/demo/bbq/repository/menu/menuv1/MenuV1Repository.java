package com.demo.bbq.repository.menu.menuv1;

import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface MenuV1Repository {

  @Headers({"Accept: application/json"})
  @GET("menu-options/{productCode}")
  Single<MenuOptionResponseWrapper> findByProductCode(@Path(value = "productCode") String productCode);

  @Headers({"Accept: application/json"})
  @GET("menu-options")
  Single<List<MenuOptionResponseWrapper>> findByCategory(@Query("category") String category);

  @POST("menu-options")
  Single<ResponseBody> save(@Body MenuOptionSaveRequestWrapper menuOptionRequest);

  @PUT("menu-options/{productCode}")
  Single<ResponseBody> update(@Path(value = "productCode") String productCode, @Body MenuOptionUpdateRequestWrapper menuOptionRequest);

  @DELETE("menu-options/{productCode}")
  Single<Response<Void>> delete(@Path(value = "productCode") String productCode);
}