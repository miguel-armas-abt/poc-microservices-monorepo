package com.demo.bbq.repository.menu.menuv1;

import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface MenuV1Repository {

  @GET("menu-options/{productCode}")
  Single<MenuOptionResponseWrapper> findByProductCode(@HeaderMap Map<String, String> headers,
                                                     @Path(value = "productCode") String productCode);

  @GET("menu-options")
  Single<List<MenuOptionResponseWrapper>> findByCategory(@HeaderMap Map<String, String> headers,
                                                         @Query("category") String category);

  default Observable<MenuOptionResponseWrapper> searchByCategory(Map<String, String> headers, String category) {
    return this.findByCategory(headers, category)
        .flatMapObservable(Observable::fromIterable);
  }

  @POST("menu-options")
  Single<ResponseBody> save(@HeaderMap Map<String, String> headers,
                            @Body MenuOptionSaveRequestWrapper menuOptionRequest);

  @PUT("menu-options/{productCode}")
  Single<ResponseBody> update(@HeaderMap Map<String, String> headers,
                              @Path(value = "productCode") String productCode,
                              @Body MenuOptionUpdateRequestWrapper menuOptionRequest);

  @DELETE("menu-options/{productCode}")
  Single<Response<Void>> delete(@HeaderMap Map<String, String> headers,
                                @Path(value = "productCode") String productCode);
}