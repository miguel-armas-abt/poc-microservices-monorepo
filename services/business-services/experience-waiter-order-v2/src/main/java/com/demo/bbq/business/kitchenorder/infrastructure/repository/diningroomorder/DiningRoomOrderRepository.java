package com.demo.bbq.business.kitchenorder.infrastructure.repository.diningroomorder;

import com.demo.bbq.business.kitchenorder.infrastructure.repository.diningroomorder.dto.DiningRoomOrderDto;
import com.demo.bbq.business.kitchenorder.infrastructure.repository.diningroomorder.dto.MenuOrderDtoRequest;
import io.smallrye.mutiny.Uni;
import javax.ws.rs.*;
import java.util.List;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/bbq/business/v1/dining-room-orders")
@RegisterRestClient(configKey="dining-room-order")
public interface DiningRoomOrderRepository {

  @GET
  @Consumes({"application/stream+json", "application/json"})
  @Produces({"application/stream+json", "application/json"})
  Uni<DiningRoomOrderDto> findByTableNumber(@QueryParam("tableNumber") Integer tableNumber);

  @PATCH
  @Consumes({"application/stream+json", "application/json"})
  @Produces({"application/stream+json", "application/json"})
  Uni<Void> update(@QueryParam("tableNumber") Integer tableNumber, List<MenuOrderDtoRequest> menuOrderRequestList);
}
