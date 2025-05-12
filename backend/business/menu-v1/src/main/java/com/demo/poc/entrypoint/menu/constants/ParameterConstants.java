package com.demo.poc.entrypoint.menu.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParameterConstants {

  public static final String CATEGORY_REGEX = "^(main|drink|dessert)$";

  public static final String CATEGORY_PARAM = "category";

  public static final String PRODUCT_CODE_PARAM = "productCode";


}
