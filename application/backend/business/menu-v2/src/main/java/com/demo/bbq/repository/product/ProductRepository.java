package com.demo.bbq.repository.product;

import com.demo.bbq.repository.product.wrapper.response.ProductResponseWrapper;
import com.demo.bbq.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.bbq.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import io.smallrye.mutiny.Uni;
import java.util.List;
import javax.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/products")
@RegisterRestClient(configKey="products")
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
