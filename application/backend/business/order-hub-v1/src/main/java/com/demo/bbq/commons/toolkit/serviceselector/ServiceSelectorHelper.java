package com.demo.bbq.commons.toolkit.serviceselector;

import com.demo.bbq.commons.errors.exceptions.SystemException;
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
        .orElseThrow(() -> new SystemException("ServiceImplNotFound"));
  }
}