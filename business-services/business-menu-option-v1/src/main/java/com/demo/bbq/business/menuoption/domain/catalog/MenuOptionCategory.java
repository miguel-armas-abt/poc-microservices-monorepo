package com.demo.bbq.business.menuoption.domain.catalog;

import com.demo.bbq.business.menuoption.domain.exception.MenuOptionException;
import java.util.Arrays;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
public enum MenuOptionCategory {

  MAIN_DISH("main-dish", "Plato principal"),
  DRINK("drink", "Bebida"),
  DESSERT("dessert", "Postre");

  private final String code;
  private final String description;

  public static Consumer<String> validateCategory =
      menuOptionCategoryCode -> Arrays.stream(MenuOptionCategory.values())
          .filter(menuOptionCategory -> menuOptionCategory.getCode().equals(menuOptionCategoryCode))
          .findFirst()
          .orElseThrow(MenuOptionException.ERROR1001::buildCustomException);

}
