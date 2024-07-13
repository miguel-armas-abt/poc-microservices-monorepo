package com.demo.bbq.rest;

import com.demo.bbq.application.service.MenuOptionService;
import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.function.Function;

@Slf4j
@Path("/bbq/business/menu/v2/menu-options")
@Produces("application/x-ndjson")
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MenuOptionRestService {

  private final MenuOptionService menuOptionService;

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
