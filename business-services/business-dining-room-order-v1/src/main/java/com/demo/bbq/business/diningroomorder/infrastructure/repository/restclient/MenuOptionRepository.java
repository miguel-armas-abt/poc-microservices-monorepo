package com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient;

import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.dto.MenuOptionDto;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface MenuOptionRepository {

  @Headers({"Accept: application/json"})
  @GET("menu-options/{id}")
  Single<MenuOptionDto> findById(@Path(value = "id") Long id);
}
