package com.demo.poc.entrypoint.menu.repository;

import com.demo.poc.commons.custom.exceptions.NoSuchMenuRepositoryStrategyException;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuRepositorySelector {

  private final List<MenuRepository> repositoryStrategies;
  private final ApplicationProperties properties;

  public MenuRepository selectStrategy() {
    return repositoryStrategies
        .stream()
        .filter(repository -> repository.supports(properties.getMenuInfo().getSelectorClass()))
        .findFirst()
        .orElseThrow(NoSuchMenuRepositoryStrategyException::new);
  }
}