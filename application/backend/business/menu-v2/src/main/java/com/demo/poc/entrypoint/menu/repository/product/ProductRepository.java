package com.demo.poc.entrypoint.menu.repository.product;

import com.demo.poc.commons.core.interceptor.restclient.RestClientRequestInterceptor;
import com.demo.poc.commons.core.interceptor.restclient.RestClientResponseInterceptor;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.poc.entrypoint.menu.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import io.smallrye.mutiny.Uni;
import java.util.List;
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/products")
@RegisterRestClient(configKey="product-v1")
@RegisterClientHeaders(ProductHeaderFactory.class)
@RegisterProvider(RestClientRequestInterceptor.class)
@RegisterProvider(RestClientResponseInterceptor.class)
public interface ProductRepository {

  @GET
  @Path("/{code}")
  @Consumes({"application/json"})
  @Produces({"application/stream+json"})
  Uni<ProductResponseWrapper> findByCode(@PathParam("code") String code);

  @GET
  @Consumes({"application/json"})
  @Produces({"application/stream+json"})
  Uni<List<ProductResponseWrapper>> findByScope(@QueryParam("scope") String scope);

  @POST
  @Consumes({"application/stream+json"})
  @Produces({"application/stream+json"})
  Uni<Void> save(ProductSaveRequestWrapper productSaveRequestWrapper);

  @DELETE
  @Consumes({"application/stream+json"})
  @Produces({"application/stream+json"})
  @Path("/{code}")
  Uni<Void> delete(@PathParam("code") String code);

  @PUT
  @Consumes({"application/stream+json"})
  @Produces({"application/stream+json"})
  @Path("/{code}")
  Uni<Void> update(@PathParam("code") String code, ProductUpdateRequestWrapper productUpdateRequestWrapper);
}