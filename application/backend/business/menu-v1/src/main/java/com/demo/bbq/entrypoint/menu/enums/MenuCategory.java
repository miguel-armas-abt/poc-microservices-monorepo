package com.demo.bbq.entrypoint.menu.enums;

import com.demo.bbq.commons.errors.exceptions.BusinessException;
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

  public static final Consumer<String> validateCategory =
      menuOptionCategoryCode -> Arrays.stream(MenuCategory.values())
          .filter(menuCategory -> menuCategory.name().equals(menuOptionCategoryCode))
          .findFirst()
          .orElseThrow(() -> new BusinessException("CategoryNotFound", "The menu category is not defined"));

}
