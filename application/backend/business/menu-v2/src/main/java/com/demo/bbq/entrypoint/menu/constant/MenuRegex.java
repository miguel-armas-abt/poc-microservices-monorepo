package com.demo.bbq.entrypoint.menu.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuRegex {

  public static final String CATEGORY = "^(main|drink|dessert)$";
}