package com.demo.poc.entrypoint.menu.rest;

import com.demo.poc.entrypoint.menu.service.MenuService;
import com.demo.poc.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.poc.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.poc.entrypoint.menu.dto.response.MenuResponseDTO;
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
@Path("/poc/business/menu/v2/menu-options")
@Produces("application/x-ndjson")
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MenuRestService {

  private final MenuService menuService;

  @GET
  @Path("/{productCode}")
  public Uni<MenuResponseDTO> findByProductCode(@PathParam("productCode") String productCode) {
    return menuService.findByProductCode(productCode);
  }

  @GET
  public Multi<MenuResponseDTO> findByCategory(@QueryParam("category") String categoryCode) {
    return menuService.findByCategory(categoryCode);
  }

  @POST
  public Uni<Response> save(MenuSaveRequestDTO menuOptionRequest) {
    return menuService.save(menuOptionRequest)
        .onItem()
        .ifNotNull()
        .transform(ignore -> Response.created(buildPostUriLocation.apply(menuOptionRequest.getProductCode())).build());
  }

  @DELETE
  @Path("/{productCode}")
  public Uni<Response> delete(@PathParam("productCode") String productCode) {
    return menuService.deleteByProductCode(productCode)
        .map(isDeleted -> Response.noContent().build());
  }

  @PUT
  @Path("/{productCode}")
  public Uni<Response> update(@PathParam("productCode") String productCode, MenuUpdateRequestDTO menuOptionRequest) {
    return menuService.update(menuOptionRequest, productCode)
        .onItem()
        .ifNotNull()
        .transform(ignore -> Response.created(buildPostUriLocation.apply(productCode)).build());
  }

  private final static Function<String, URI> buildPostUriLocation = productCode ->
      UriBuilder.fromResource(MenuRestService.class).path("/{productCode}").build(productCode);

}
