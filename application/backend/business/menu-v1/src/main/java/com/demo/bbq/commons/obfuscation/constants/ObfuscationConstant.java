package com.demo.bbq.commons.obfuscation.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObfuscationConstant {

  public static String OBFUSCATION_WARNING = "obfuscation.failed";

  public static String OBFUSCATION_MASK = "****";

  public static final String ARRAY_WILDCARD = "[*]";

  public static final String JSON_SEGMENT_SPLITTER_REGEX = "\\.";
}
