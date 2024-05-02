package com.demo.bbq.repository.menu.menuv2;

import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import com.demo.bbq.utils.restclient.retrofit.ReactiveTransformer;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface MenuV2Repository {

  @Streaming
  @GET("menu-options/{productCode}")
  Single<MenuOptionResponseWrapper> findByProductCode(@HeaderMap Map<String, String> headers,
                                                     @Path(value = "productCode") String productCode);

  @Streaming
  @GET("menu-options")
  Observable<ResponseBody> findByCategory(@HeaderMap Map<String, String> headers,
                                          @Query("category") String category);

  default Observable<MenuOptionResponseWrapper> searchByCategory(Map<String, String> headers, String category) {
    return this.findByCategory(headers, category)
        .compose(ReactiveTransformer.of(MenuOptionResponseWrapper.class));
  }

  @Streaming
  @POST("menu-options")
  Single<ResponseBody> save(@HeaderMap Map<String, String> headers,
                            @Body MenuOptionSaveRequestWrapper menuOptionRequest);

  @Streaming
  @PUT("menu-options/{productCode}")
  Single<ResponseBody> update(@HeaderMap Map<String, String> headers,
                              @Path(value = "productCode") String productCode,
                              @Body MenuOptionUpdateRequestWrapper menuOptionRequest);

  @Streaming
  @DELETE("menu-options/{productCode}")
  Single<Response<Void>> delete(@HeaderMap Map<String, String> headers,
                                @Path(value = "productCode") String productCode);
}