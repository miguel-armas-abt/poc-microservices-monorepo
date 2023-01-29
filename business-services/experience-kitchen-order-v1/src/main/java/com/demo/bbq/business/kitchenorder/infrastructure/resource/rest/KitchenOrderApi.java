package com.demo.bbq.business.kitchenorder.infrastructure.resource.rest;

import com.demo.bbq.business.kitchenorder.application.service.KitchenOrderService;
import com.demo.bbq.business.kitchenorder.domain.model.response.MenuOrder;
import io.smallrye.mutiny.Multi;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("/bbq/business/v2/kitchen-orders")
@Produces("application/stream+json")
@Consumes(MediaType.APPLICATION_JSON)
public class KitchenOrderApi {

  @Inject
  KitchenOrderService kitchenOrderService;

  @GET
  public Multi<MenuOrder> findByTableNumber(@QueryParam("tableNumber") Integer tableNumber) {
    return kitchenOrderService.findByCategory(tableNumber);
  }

}