package com.demo.bbq.business.invoice.infrastructure.repository.restclient;

import com.demo.bbq.business.invoice.infrastructure.repository.restclient.dto.menuoption.MenuOptionDto;
import io.reactivex.Single;
import retrofit2.http.*;

public interface MenuOptionApi {

  @Headers({"Accept: application/json"})
  @GET("menu-options/{id}")
  Single<MenuOptionDto> findById(@Path(value = "id") Long id);
}
