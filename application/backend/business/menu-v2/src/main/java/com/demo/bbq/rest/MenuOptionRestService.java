package com.demo.bbq.rest;

import com.demo.bbq.application.service.MenuOptionService;
import com.demo.bbq.application.dto.request.MenuOptionSaveRequestDTO;
import com.demo.bbq.application.dto.request.MenuOptionUpdateRequestDTO;
import com.demo.bbq.application.dto.response.MenuOptionResponseDTO;
import com.demo.bbq.utils.toolkit.HeaderPlanerUtil;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.net.URI;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Path("/bbq/business/menu/v2/menu-options")
@Produces("application/x-ndjson")
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MenuOptionRestService {

  private final MenuOptionService menuOptionService;

  @Context
  HttpHeaders httpHeaders;

  @GET
  @Path("/{productCode}")
  public Uni<MenuOptionResponseDTO> findByProductCode(@PathParam("productCode") String productCode) {
    Map<String, String> headers = HeaderPlanerUtil.flatHeaders(httpHeaders.getRequestHeaders());
    return menuOptionService.findByProductCode(productCode, headers);
  }

  @GET
  public Multi<MenuOptionResponseDTO> findByCategory(@QueryParam("category") String categoryCode) {
    Map<String, String> headers = HeaderPlanerUtil.flatHeaders(httpHeaders.getRequestHeaders());
    return menuOptionService.findByCategory(categoryCode, headers);
  }

  @POST
  public Uni<Response> save(MenuOptionSaveRequestDTO menuOptionRequest) {
    Map<String, String> headers = HeaderPlanerUtil.flatHeaders(httpHeaders.getRequestHeaders());
    return menuOptionService.save(menuOptionRequest, headers)
        .onItem()
        .ifNotNull()
        .transform(ignore -> Response.created(buildPostUriLocation.apply(menuOptionRequest.getProductCode())).build());
  }

  @DELETE
  @Path("/{productCode}")
  public Uni<Response> delete(@PathParam("productCode") String productCode) {
    Map<String, String> headers = HeaderPlanerUtil.flatHeaders(httpHeaders.getRequestHeaders());
    return menuOptionService.deleteByProductCode(productCode, headers)
        .map(isDeleted -> Response.noContent().build());
  }

  @PUT
  @Path("/{productCode}")
  public Uni<Response> update(@PathParam("productCode") String productCode, MenuOptionUpdateRequestDTO menuOptionRequest) {
    Map<String, String> headers = HeaderPlanerUtil.flatHeaders(httpHeaders.getRequestHeaders());
    return menuOptionService.update(menuOptionRequest, productCode, headers)
        .onItem()
        .ifNotNull()
        .transform(ignore -> Response.created(buildPostUriLocation.apply(productCode)).build());
  }

  private final static Function<String, URI> buildPostUriLocation = productCode ->
      UriBuilder.fromResource(MenuOptionRestService.class).path("/{productCode}").build(productCode);

}
