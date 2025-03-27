package com.demo.poc.service.product;

import static com.demo.poc.commons.TemplateConfig.readJSON;
import static com.demo.poc.commons.toolkit.delay.DelayGenerator.generateRandomDelay;
import static com.demo.poc.commons.toolkit.headers.HeadersGenerator.contentType;
import static com.demo.poc.commons.toolkit.headers.HeadersGenerator.generateTraceId;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.demo.poc.commons.MockRuleProvider;
import java.util.concurrent.TimeUnit;
import org.apache.http.entity.ContentType;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceMock implements MockRuleProvider {

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
  }
}