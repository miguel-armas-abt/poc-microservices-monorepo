package com.demo.service.entrypoint.menu.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Regex {

  public static final String CATEGORY_REGEX = "^(main|drink|dessert)$";
}