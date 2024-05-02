package com.demo.bbq.repository.tableorder;

import com.demo.bbq.repository.tableorder.wrapper.TableOrderRequestWrapper;
import com.demo.bbq.application.dto.tableorder.request.MenuOrderRequestDTO;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.*;

public interface TableOrderRepository {

  @Streaming
  @PATCH("table-orders")
  Single<ResponseBody> generateTableOrder(@HeaderMap Map<String, String> headers,
                                          @Body List<MenuOrderRequestDTO> requestedMenuOrderList,
                                          @Query("tableNumber") Integer tableNumber);

  @Streaming
  @GET("table-orders")
  Single<TableOrderRequestWrapper> findByTableNumber(@HeaderMap Map<String, String> headers,
                                                     @Query("tableNumber") Integer tableNumber);

  @Streaming
  @DELETE("table-orders")
  Single<Response<Void>> cleanTable(@HeaderMap Map<String, String> headers,
                                    @Query("tableNumber") Integer tableNumber);

}
