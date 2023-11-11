package com.demo.bbq.business.orderhub.infrastructure.resource.rest.menu;

import com.demo.bbq.business.orderhub.infrastructure.documentation.data.OrderHubMetadata;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import io.reactivex.Observable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.servlet.http.HttpServletRequest;

public interface MenuRestService {

  @Operation(summary = "Get a menu list by category", tags = OrderHubMetadata.TAG)
  @ApiResponse(responseCode = "200")
  Observable<MenuOptionDto> findMenuByCategory(HttpServletRequest servletRequest,String categoryCode);

}
