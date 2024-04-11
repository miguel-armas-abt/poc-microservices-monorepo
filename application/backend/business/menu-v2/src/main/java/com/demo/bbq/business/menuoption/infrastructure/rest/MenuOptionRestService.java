package com.demo.bbq.business.menuoption.infrastructure.rest;

import com.demo.bbq.business.menuoption.application.service.MenuOptionService;
import com.demo.bbq.business.menuoption.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.business.menuoption.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.business.menuoption.application.dto.response.MenuOptionResponseDTO;
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
@Path("/bbq/business/menu/v2/menu-options")
@Produces("application/stream+json")
@Consumes(MediaType.APPLICATION_JSON)
public class MenuOptionRestService {

  @Inject
  MenuOptionService menuOptionService;

  @GET
  @Path("/{productCode}")
  public Uni<MenuOptionResponseDTO> findByProductCode(@PathParam("productCode") String productCode) {
    return menuOptionService.findByProductCode(productCode);
  }

  @GET
  public Multi<MenuOptionResponseDTO> findByCategory(@QueryParam("category") String categoryCode) {
    return menuOptionService.findByCategory(categoryCode);
  }

  @POST
  public Uni<Response> save(MenuOptionSaveRequestDTO menuOptionRequest) {
    return menuOptionService.save(menuOptionRequest)
        .onItem()
        .ifNotNull()
        .transform(ignore -> Response.created(buildPostUriLocation.apply(menuOptionRequest.getProductCode())).build());
  }

  @DELETE
  @Path("/{productCode}")
  public Uni<Response> delete(@PathParam("productCode") String productCode) {
    return menuOptionService.deleteByProductCode(productCode)
        .map(isDeleted -> Response.noContent().build());
  }

  @PUT
  @Path("/{productCode}")
  public Uni<Response> update(@PathParam("productCode") String productCode, MenuOptionUpdateRequestDTO menuOptionRequest) {
    return menuOptionService.update(menuOptionRequest, productCode)
        .onItem()
        .ifNotNull()
        .transform(ignore -> Response.created(buildPostUriLocation.apply(productCode)).build());
  }

  private final static Function<String, URI> buildPostUriLocation = productCode ->
      UriBuilder.fromResource(MenuOptionRestService.class).path("/{productCode}").build(productCode);

}
