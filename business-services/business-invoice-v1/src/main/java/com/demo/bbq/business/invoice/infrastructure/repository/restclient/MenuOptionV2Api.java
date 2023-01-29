package com.demo.bbq.business.invoice.infrastructure.repository.restclient;

import com.demo.bbq.business.invoice.infrastructure.repository.restclient.dto.menuoption.MenuOptionDto;
import io.reactivex.Single;
import retrofit2.http.*;

public interface MenuOptionV2Api {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("menu-options/{id}")
  Single<MenuOptionDto> findById(@Path(value = "id") Long id);
}
