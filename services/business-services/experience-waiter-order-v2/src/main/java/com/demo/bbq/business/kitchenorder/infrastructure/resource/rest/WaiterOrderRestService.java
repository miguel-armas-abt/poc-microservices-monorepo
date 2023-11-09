package com.demo.bbq.business.kitchenorder.infrastructure.resource.rest;

import com.demo.bbq.business.kitchenorder.application.service.WaiterOrderService;
import com.demo.bbq.business.kitchenorder.domain.model.response.MenuOrder;
import io.smallrye.mutiny.Multi;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("/bbq/experience/v1/order-waiters")
@Produces("application/stream+json")
@Consumes(MediaType.APPLICATION_JSON)
public class WaiterOrderRestService {

  @Inject
  WaiterOrderService waiterOrderService;

  @GET
  public Multi<MenuOrder> findByTableNumber(@QueryParam("tableNumber") Integer tableNumber) {
    return waiterOrderService.findByCategory(tableNumber);
  }

}
