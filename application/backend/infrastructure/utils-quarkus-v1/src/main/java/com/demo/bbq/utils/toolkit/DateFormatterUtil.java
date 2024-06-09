package com.demo.bbq.utils.toolkit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateFormatterUtil {

  public static final String DEFAULT_DATE_PATTERN = "yyyyMMddHHmmssSSS";

  public static final UnaryOperator<String> getFormattedDate = pattern ->
      LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));

}
