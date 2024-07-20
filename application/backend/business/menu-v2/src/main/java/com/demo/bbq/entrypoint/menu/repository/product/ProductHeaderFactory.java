package com.demo.bbq.entrypoint.menu.repository.product;

import com.demo.bbq.config.properties.ApplicationProperties;
import com.demo.bbq.commons.properties.dto.HeaderTemplate;
import com.demo.bbq.commons.restclient.headers.HeadersBuilderUtil;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

@Provider
@RequiredArgsConstructor
public class ProductHeaderFactory implements ClientHeadersFactory {

  private static final String SERVICE_NAME_PRODUCT = "product-v1";
  private final ApplicationProperties properties;

  @Override
  public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders,
                                               MultivaluedMap<String, String> outgoingHeaders) {

    HeaderTemplate headerTemplate = properties.restClients().get(SERVICE_NAME_PRODUCT).request().headers();
    return HeadersBuilderUtil.buildHeaders(headerTemplate, incomingHeaders);
  }
}