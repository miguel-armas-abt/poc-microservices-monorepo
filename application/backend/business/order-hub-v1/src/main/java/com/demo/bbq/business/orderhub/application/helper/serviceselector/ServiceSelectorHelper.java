package com.demo.bbq.business.orderhub.application.helper.serviceselector;

import com.demo.bbq.support.exception.model.ApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceSelectorHelper<T extends SelectedServiceBase> {

  private final List<T> serviceList;

  public T getService(Class<?> selectorClass) {
    return serviceList.stream()
        .filter(service -> service.supports(selectorClass))
        .findFirst()
        .orElseThrow(() -> ApiException.builder()
            .message("No service implementation found")
            .build());
  }
}