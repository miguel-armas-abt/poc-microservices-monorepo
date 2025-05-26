package com.demo.poc.entrypoint.product;

import static com.demo.poc.commons.utils.JsonReader.readJSON;
import static com.demo.poc.commons.utils.DelayGenerator.generateRandomDelay;
import static com.demo.poc.commons.utils.HeadersGenerator.contentType;
import static com.demo.poc.commons.utils.HeadersGenerator.generateTraceId;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.demo.poc.commons.config.MockService;
import java.util.concurrent.TimeUnit;
import org.apache.http.entity.ContentType;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public class ProductMockService implements MockService {

  @Override
  public void loadMocks(ClientAndServer mockServer) {

    mockServer
        .when(request()
            .withMethod("GET")
            .withPath("/poc/business/product/v1/products"))
        .respond(request -> {

          long randomDelay = generateRandomDelay();
          Header traceIdHeader = generateTraceId();

          return response()
              .withStatusCode(HttpStatusCode.OK_200.code())
              .withHeader(contentType(ContentType.APPLICATION_JSON.getMimeType()))
              .withHeader(traceIdHeader)
              .withBody(readJSON("mocks/product-v1/get-products.200.json"))
              .withDelay(TimeUnit.MILLISECONDS, randomDelay);
        });

      mockServer
              .when(request()
                      .withMethod("GET")
                      .withPath("/poc/business/product/v1/products/.*"))
              .respond(request -> {

                  long randomDelay = generateRandomDelay();
                  Header traceIdHeader = generateTraceId();

                  return response()
                          .withStatusCode(HttpStatusCode.OK_200.code())
                          .withHeader(contentType(ContentType.APPLICATION_JSON.getMimeType()))
                          .withHeader(traceIdHeader)
                          .withBody(readJSON("mocks/product-v1/get-product-by-id.200.json"))
                          .withDelay(TimeUnit.MILLISECONDS, randomDelay);
              });
  }
}