package com.demo.bbq.business.kitchenorder.infrastructure.repository.menuoptionv2;

import com.demo.bbq.business.kitchenorder.infrastructure.repository.menuoptionv2.dto.MenuOptionDto;
import com.demo.bbq.business.kitchenorder.infrastructure.repository.menuoptionv2.dto.MenuOptionRequestDto;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/bbq/business/v2/menu-options")
@RegisterRestClient(configKey="menu-option")
public interface MenuOptionRepository {

  @GET
  @Path("/{id}")
  @Consumes({"application/stream+json", "application/json"})
  @Produces({"application/stream+json", "application/json"})
  Uni<MenuOptionDto> findById(@PathParam("id") Long id);

  @GET
  @Consumes({"application/stream+json", "application/json"})
  @Produces({"application/stream+json", "application/json"})
  Multi<MenuOptionDto> findByCategory(@QueryParam("category") String category);

  @POST
  @Consumes({"application/stream+json", "application/json"})
  @Produces({"application/stream+json", "application/json"})
  Uni<Void> save(MenuOptionRequestDto menuOptionRequestDto);

  @DELETE
  @Consumes({"application/stream+json", "application/json"})
  @Produces({"application/stream+json", "application/json"})
  @Path("/{id}")
  Uni<Void> delete(@PathParam("id") Long id);

  @PUT
  @Consumes({"application/stream+json", "application/json"})
  @Produces({"application/stream+json", "application/json"})
  @Path("/{id}")
  Uni<Void> update(@PathParam("id") Long id, MenuOptionRequestDto menuOptionRequestDto);
}
