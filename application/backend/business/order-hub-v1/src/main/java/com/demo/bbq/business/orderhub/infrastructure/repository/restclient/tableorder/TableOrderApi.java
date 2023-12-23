package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto.TableOrderDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto.MenuOrderRequestDto;
import io.reactivex.Single;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface TableOrderApi {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @PATCH("table-orders")
  Single<ResponseBody> generateTableOrder(@Body List<MenuOrderRequestDto> requestedMenuOrderList, @Query("tableNumber") Integer tableNumber);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("table-orders")
  Single<TableOrderDto> findByTableNumber(@Query("tableNumber") Integer tableNumber);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @DELETE("table-orders")
  Single<Response<Void>> cleanTable(@Query("tableNumber") Integer tableNumber);

}
