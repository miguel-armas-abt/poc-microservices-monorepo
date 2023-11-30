package com.demo.bbq.business.menuoption.infrastructure.repository.restclient;

import com.demo.bbq.business.menuoption.infrastructure.repository.restclient.dto.ProductDto;
import com.demo.bbq.business.menuoption.infrastructure.repository.restclient.dto.ProductSaveRequestDto;
import com.demo.bbq.business.menuoption.infrastructure.repository.restclient.dto.ProductUpdateRequestDto;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import javax.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/bbq/business/product/v1/products")
@RegisterRestClient(configKey="products")
public interface ProductApi {

  @GET
  @Path("/{code}")
  @Consumes({"application/json"})
  @Produces({"application/stream+json"})
  Uni<ProductDto> findByCode(@PathParam("code") String code);

  @GET
  @Consumes({"application/json"})
  @Produces({"application/stream+json"})
  Uni<List<ProductDto>> findByScope(@QueryParam("scope") String scope);

  @POST
  @Consumes({"application/stream+json"})
  @Produces({"application/stream+json"})
  Uni<Void> save(ProductSaveRequestDto productSaveRequestDto);

  @DELETE
  @Consumes({"application/stream+json"})
  @Produces({"application/stream+json"})
  @Path("/{code}")
  Uni<Void> delete(@PathParam("code") String code);

  @PUT
  @Consumes({"application/stream+json"})
  @Produces({"application/stream+json"})
  @Path("/{code}")
  Uni<Void> update(@PathParam("code") String code, ProductUpdateRequestDto productUpdateRequestDto);
}
