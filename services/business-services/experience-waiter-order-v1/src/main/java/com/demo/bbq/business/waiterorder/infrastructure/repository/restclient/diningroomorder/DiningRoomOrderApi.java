package com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.diningroomorder;

import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.diningroomorder.dto.DiningRoomOrderDto;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.diningroomorder.dto.MenuOrderRequestDto;
import io.reactivex.Single;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface DiningRoomOrderApi {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @PATCH("dining-room-orders")
  Single<ResponseBody> generateTableOrder(@Body List<MenuOrderRequestDto> requestedMenuOrderList, @Query("tableNumber") Integer tableNumber);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("dining-room-orders")
  Single<DiningRoomOrderDto> findByTableNumber(@Query("tableNumber") Integer tableNumber);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @PATCH("dining-room-orders/clean")
  Single<ResponseBody> cleanTable(@Query("tableNumber") Integer tableNumber);

}
