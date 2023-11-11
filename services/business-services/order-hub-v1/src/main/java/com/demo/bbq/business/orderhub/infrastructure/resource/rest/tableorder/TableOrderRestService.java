package com.demo.bbq.business.orderhub.infrastructure.resource.rest.tableorder;

import com.demo.bbq.business.orderhub.infrastructure.documentation.data.OrderHubMetadata;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto.MenuOrderRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto.TableOrderDto;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface TableOrderRestService {

  @Operation(summary = "Generate an order of menus for a table", tags = OrderHubMetadata.TAG)
  @ApiResponse(responseCode = "201")
  Completable generateTableOrder(HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse,
                                 List<MenuOrderRequestDto> requestedMenuOrderList,
                                 Integer tableNumber);

  @Operation(summary = "Get an order list by table number", tags = OrderHubMetadata.TAG)
  @ApiResponse(responseCode = "201")
  Single<TableOrderDto> findByTableNumber(HttpServletRequest servletRequest,
                                          HttpServletResponse servletResponse,
                                          Integer tableNumber);

}
