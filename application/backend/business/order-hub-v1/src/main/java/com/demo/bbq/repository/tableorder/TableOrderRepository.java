package com.demo.bbq.repository.tableorder;

import com.demo.bbq.repository.tableorder.wrapper.TableOrderRequestWrapper;
import com.demo.bbq.application.dto.tableorder.request.MenuOrderRequestDTO;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface TableOrderRepository {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @PATCH("table-orders")
  Single<ResponseBody> generateTableOrder(@Body List<MenuOrderRequestDTO> requestedMenuOrderList, @Query("tableNumber") Integer tableNumber);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("table-orders")
  Single<TableOrderRequestWrapper> findByTableNumber(@Query("tableNumber") Integer tableNumber);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @DELETE("table-orders")
  Single<Response<Void>> cleanTable(@Query("tableNumber") Integer tableNumber);

}
