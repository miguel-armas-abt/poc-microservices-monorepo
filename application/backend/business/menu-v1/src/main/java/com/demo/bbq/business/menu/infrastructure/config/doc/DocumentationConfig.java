package com.demo.bbq.business.menu.infrastructure.config.doc;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentationConfig {

  public static final String TAG = "menu";

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("BBQ")
        .addOpenApiCustomiser(openApi -> {
          openApi.setInfo(new Info().title("menu-v1").description("This API allows you to manage menus."));
        })
        .build();
  }

  public static class DocumentationExample {
    public static final String PRODUCT_CODE = "MENU0001";
    public static final String DESCRIPTION = "Pollo a la brasa";
    public static final String CATEGORY_MAIN_DISH = "MAIN";
    public static final String CATEGORY_DESSERT = "DESSERT";
    public static final String CATEGORY_DRINK = "DRINK";
    public static final String UNIT_PRICE = "29.99";
  }
}
