package com.demo.bbq.business.tableplacement.infrastructure.resource.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import com.demo.bbq.business.tableplacement.domain.model.response.TableOrder;
import com.demo.bbq.business.tableplacement.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.tableplacement.infrastructure.documentation.data.TablePlacementDocumentationMetadata;
import com.demo.bbq.business.tableplacement.infrastructure.documentation.data.TablePlacementExample;
import com.demo.bbq.support.exception.documentation.ApiExceptionJsonExample;
import com.demo.bbq.support.exception.model.dto.ApiExceptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Mono;

@ApiResponses(value = {
    @ApiResponse(responseCode = "400",
        content = @Content(schema = @Schema(implementation = ApiExceptionDto.class), examples = @ExampleObject(value = ApiExceptionJsonExample.BAD_REQUEST))),
    @ApiResponse(responseCode = "404",
        content = @Content(schema = @Schema(implementation = ApiExceptionDto.class), examples = @ExampleObject(value = ApiExceptionJsonExample.NOT_FOUND))),
})
public interface TablePlacementRestService {

  @Operation(summary = "Get an order list by table number", tags = TablePlacementDocumentationMetadata.TAG)
  @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TableOrder.class))})
  Mono<TableOrder> findByTableNumber(HttpServletRequest servletRequest,
                                     @Parameter(example = TablePlacementExample.TABLE_NUMBER) Integer tableNumber);

  @Operation(summary = "Generate an order of menus for a table", tags = TablePlacementDocumentationMetadata.TAG)
  @ApiResponse(responseCode = "201")
  Mono<Void> generateTableOrder(HttpServletRequest servletRequest,
                                HttpServletResponse servletResponse,
                                @RequestBody(description = "Requested menu list") List<MenuOrderRequest> requestedMenuOrderList,
                                @Parameter(example = TablePlacementExample.TABLE_NUMBER) Integer tableNumber);

  @Operation(summary = "Clean the menu list associated to table order", tags = TablePlacementDocumentationMetadata.TAG)
  @ApiResponse(responseCode = "201")
  Mono<Void> cleanTable(HttpServletRequest servletRequest,
                        HttpServletResponse servletResponse,
                        @Parameter(example = TablePlacementExample.TABLE_NUMBER) Integer tableNumber);

}
