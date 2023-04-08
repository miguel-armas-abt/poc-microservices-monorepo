package com.demo.bbq.business.invoice.infrastructure.repository.restclient;

import com.demo.bbq.business.invoice.infrastructure.repository.restclient.dto.diningroomorder.DiningRoomOrderDto;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface DiningRoomOrderApi {

  @Headers({"Accept: application/stream+json"})
  @GET("dining-room-orders")
  Single<DiningRoomOrderDto> findByTableNumber(@Query("tableNumber") Integer tableNumber);
}
