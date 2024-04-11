package com.demo.bbq.business.menuoption.application.catalog;

import com.demo.bbq.business.menuoption.domain.exception.MenuOptionException;
import java.util.Arrays;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MenuCategory {

  MAIN("Plato principal"),
  DRINK("Bebida"),
  DESSERT("Postre");

  private final String description;

  public static final Consumer<String> validateCategory =
      menuOptionCategoryCode -> Arrays.stream(MenuCategory.values())
          .filter(menuCategory -> menuCategory.name().equals(menuOptionCategoryCode))
          .findFirst()
          .orElseThrow(MenuOptionException.ERROR0001::buildException);

}

