package com.demo.bbq.business.invoice.infrastructure.repository.restclient;

import com.demo.bbq.business.invoice.infrastructure.repository.restclient.dto.diningroomorder.DiningRoomOrderDto;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface DiningRoomOrderApi {

  @Headers({"Accept: application/stream+json"})
  @GET("dining-room-orders")
  Single<DiningRoomOrderDto> findByTableNumber(@Query("tableNumber") Integer tableNumber);

  @Streaming
  @PATCH("dining-room-orders/clean")
  Single<ResponseBody> cleanTable(@Query("tableNumber") Integer tableNumber);
}
