package com.demo.poc.entrypoint.menu.repository;

import com.demo.poc.commons.custom.exceptions.NoSuchMenuRepositoryStrategyException;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuRepositorySelector {

  private static final String MENU_REPOSITORY_SELECTOR = "menu-repository";

  private final List<MenuRepository> repositoryStrategies;
  private final ApplicationProperties properties;

  public MenuRepository selectStrategy() {
    return repositoryStrategies
        .stream()
        .filter(repository -> repository.supports(properties.getCustom().getSelectorClass().get(MENU_REPOSITORY_SELECTOR)))
        .findFirst()
        .orElseThrow(NoSuchMenuRepositoryStrategyException::new);
  }
}