package com.demo.bbq.repository.product;

import com.demo.bbq.config.toolkit.HeaderRequestFilter;
import com.demo.bbq.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.bbq.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.bbq.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import com.demo.bbq.utils.tracing.logging.RestClientRequestLogger;
import com.demo.bbq.utils.tracing.logging.RestClientResponseLogger;
import io.smallrye.mutiny.Uni;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/products")
@RegisterRestClient(configKey="products")
@RegisterProvider(RestClientRequestLogger.class)
@RegisterProvider(RestClientResponseLogger.class)
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

  static ProductRepository create(URI uri, Map<String, String> headers) {
    return RestClientBuilder.newBuilder()
        .baseUri(uri)
        .register(new HeaderRequestFilter(headers))
        .build(ProductRepository.class);
  }
}