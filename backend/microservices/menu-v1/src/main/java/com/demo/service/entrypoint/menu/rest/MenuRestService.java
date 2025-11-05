package com.demo.service.entrypoint.menu.rest;

import com.demo.commons.validations.ParamValidator;
import com.demo.commons.validations.headers.DefaultHeaders;
import com.demo.service.entrypoint.menu.dto.request.MenuSaveRequestDto;
import com.demo.service.entrypoint.menu.dto.response.MenuResponseDto;
import com.demo.service.entrypoint.menu.service.MenuService;
import com.demo.service.entrypoint.menu.dto.request.MenuUpdateRequestDto;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
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
@Path("/poc/business/menu/v1/menu-options")
@Produces("application/x-ndjson")
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MenuRestService {

  private final MenuService menuService;
  private final ParamValidator paramValidator;

  @Context
  HttpHeaders httpHeaders;

  @GET
  @Path("/{productCode}")
  public Uni<MenuResponseDto> findByProductCode(@PathParam("productCode") String productCode) {
    return paramValidator.validateHeadersAndGet(httpHeaders.getRequestHeaders(), DefaultHeaders.class)
        .flatMap(defaultHeaders -> menuService.findByProductCode(productCode));
  }

  @GET
  public Multi<MenuResponseDto> findByCategory(@QueryParam("category") String categoryCode) {
    return paramValidator.validateHeadersAndGet(httpHeaders.getRequestHeaders(), DefaultHeaders.class)
        .onItem()
        .transformToMulti(defaultHeaders -> menuService.findByCategory(categoryCode));
  }

  @POST
  public Uni<Response> save(MenuSaveRequestDto menuOptionRequest) {
    return paramValidator.validateHeadersAndGet(httpHeaders.getRequestHeaders(), DefaultHeaders.class)
        .flatMap(defaultHeaders -> menuService.save(menuOptionRequest))
        .onItem()
        .ifNotNull()
        .transform(ignore -> Response.created(buildPostUriLocation.apply(menuOptionRequest.getProductCode())).build());
  }

  @DELETE
  @Path("/{productCode}")
  public Uni<Response> delete(@PathParam("productCode") String productCode) {
    return paramValidator.validateHeadersAndGet(httpHeaders.getRequestHeaders(), DefaultHeaders.class)
        .flatMap(defaultHeaders -> menuService.deleteByProductCode(productCode))
        .map(isDeleted -> Response.noContent().build());
  }

  @PUT
  @Path("/{productCode}")
  public Uni<Response> update(@PathParam("productCode") String productCode, MenuUpdateRequestDto menuOptionRequest) {
    return paramValidator.validateHeadersAndGet(httpHeaders.getRequestHeaders(), DefaultHeaders.class)
        .flatMap(defaultHeaders -> menuService.update(menuOptionRequest, productCode))
        .onItem()
        .ifNotNull()
        .transform(ignore -> Response.created(buildPostUriLocation.apply(productCode)).build());
  }

  private final static Function<String, URI> buildPostUriLocation = productCode ->
      UriBuilder.fromResource(MenuRestService.class).path("/{productCode}").build(productCode);

}
