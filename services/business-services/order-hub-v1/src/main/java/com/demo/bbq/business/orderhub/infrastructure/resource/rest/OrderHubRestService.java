package com.demo.bbq.business.orderhub.infrastructure.resource.rest;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.diningroomorder.dto.DiningRoomOrderDto;
import com.demo.bbq.business.orderhub.infrastructure.documentation.data.OrderHubMetadata;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.diningroomorder.dto.MenuOrderRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menuoption.dto.MenuOptionDto;
import com.demo.bbq.support.exception.documentation.ApiExceptionJsonExample;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ApiResponses(value = {
    @ApiResponse(responseCode = "400",
        content = @Content(schema = @Schema(implementation = ApiExceptionDto.class), examples = @ExampleObject(value = ApiExceptionJsonExample.BAD_REQUEST))),
    @ApiResponse(responseCode = "404",
        content = @Content(schema = @Schema(implementation = ApiExceptionDto.class), examples = @ExampleObject(value = ApiExceptionJsonExample.NOT_FOUND))),
})
public interface OrderHubRestService {

  @Operation(summary = "Get a menu list by category", tags = OrderHubMetadata.TAG)
  @ApiResponse(responseCode = "200")
  Flux<MenuOptionDto> findMenuByCategory(HttpServletRequest servletRequest, String menuCategory);

  @Operation(summary = "Generate an order of menus for a table", tags = OrderHubMetadata.TAG)
  @ApiResponse(responseCode = "201")
  Mono<Void> generateTableOrder(HttpServletRequest servletRequest,
                                HttpServletResponse servletResponse,
                                List<MenuOrderRequestDto> requestedMenuOrderList,
                                Integer tableNumber);

  @Operation(summary = "Get an order list by table number", tags = OrderHubMetadata.TAG)
  @ApiResponse(responseCode = "201")
  Mono<DiningRoomOrderDto> findByTableNumber(HttpServletRequest servletRequest,
                                              HttpServletResponse servletResponse,
                                              Integer tableNumber);

}
