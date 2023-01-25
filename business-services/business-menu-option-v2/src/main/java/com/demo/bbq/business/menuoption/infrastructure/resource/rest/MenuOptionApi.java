package com.demo.bbq.business.menuoption.infrastructure.resource.rest;

import com.demo.bbq.business.menuoption.application.service.MenuOptionService;
import com.demo.bbq.business.menuoption.domain.model.request.MenuOptionRequest;
import com.demo.bbq.business.menuoption.domain.model.response.MenuOption;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.function.Function;

@Slf4j
@Path("/bbq/business/v2/menu-options")
@Produces("application/stream+json")
@Consumes(MediaType.APPLICATION_JSON)
public class MenuOptionApi {

  @Inject
  MenuOptionService menuOptionService;

  @GET
  @Path("/{id}")
  public Uni<MenuOption> findById(@PathParam("id") Long id) {
    return menuOptionService.findById(id);
  }

  @GET
  public Multi<MenuOption> findByCategory(@QueryParam("category") String categoryCode) {
    return menuOptionService.findByCategory(categoryCode);
  }

  @POST
  public Uni<Response> save(MenuOptionRequest menuOptionRequest) {
    return menuOptionService.save(menuOptionRequest)
        .onItem()
        .ifNotNull()
        .transform(menuOptionId -> Response.created(buildPostUriLocation.apply(menuOptionId)).build());
  }

  @DELETE
  @Path("/{id}")
  public Uni<Response> delete(@PathParam("id") Long id) {
    return menuOptionService.deleteById(id)
        .map(isDeleted -> (isDeleted
            ? Response.noContent()
            : Response.status(Response.Status.BAD_REQUEST))
            .build());
  }

  @PUT
  @Path("/{id}")
  public Uni<Response> update(@PathParam("id") Long id, MenuOptionRequest menuOptionRequest) {
    return menuOptionService.update(menuOptionRequest, id)
        .onItem()
        .ifNotNull()
        .transform(menuOptionId -> Response.created(buildPostUriLocation.apply(menuOptionId)).build());
  }

  private final static Function<Long, URI> buildPostUriLocation = menuOptionId ->
      UriBuilder.fromResource(MenuOptionApi.class).path("/{id}").build(menuOptionId);

}