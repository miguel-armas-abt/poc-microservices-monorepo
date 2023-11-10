package com.demo.bbq.business.menu.domain.catalog;

import com.demo.bbq.business.menu.domain.exception.MenuOptionException;
import java.util.Arrays;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
public enum MenuCategory {

  MAIN("Plato principal"),
  DRINK("Bebida"),
  DESSERT("Postre");

  private final String description;

  public static Consumer<String> validateCategory =
      menuOptionCategoryCode -> Arrays.stream(MenuCategory.values())
          .filter(menuCategory -> menuCategory.name().equals(menuOptionCategoryCode))
          .findFirst()
          .orElseThrow(MenuOptionException.ERROR0001::buildException);

}
