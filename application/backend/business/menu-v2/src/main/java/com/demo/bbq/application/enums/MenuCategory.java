package com.demo.bbq.application.enums;

import com.demo.bbq.config.utils.errors.exceptions.BusinessException;
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
          .orElseThrow(() -> new BusinessException("The menu category is not defined"));
}

