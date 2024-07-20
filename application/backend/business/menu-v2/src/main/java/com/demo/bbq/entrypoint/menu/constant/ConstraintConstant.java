package com.demo.bbq.entrypoint.menu.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstraintConstant {

  public static final String CATEGORY_REGEX = "^(main|drink|dessert)$";

  public static final String CATEGORY_INVALID_MESSAGE = "invalid category";
  public static final String CATEGORY_NOT_BLANK_MESSAGE = "category cannot be null or empty";
  public static final String PRODUCT_CODE_NOT_BLANK_MESSAGE = "productCode cannot be null or empty";
  public static final String MENU_DESCRIPTION_NOT_BLANK_MESSAGE = "description cannot be null or empty";
  public static final String UNIT_PRICE_NOT_NULL_MESSAGE = "unitPrice cannot be null";
}