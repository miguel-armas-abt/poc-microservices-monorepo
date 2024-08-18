package com.demo.bbq.commons.toolkit.params.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.function.UnaryOperator;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimestampParamUtil {

  public static final String DEFAULT_DATE_PATTERN = "yyyyMMddHHmmssSSS";

  public static final UnaryOperator<String> formatDate = pattern ->
      LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));

  public static XMLGregorianCalendar getGregorianCalendar() {
    try {
      DatatypeFactory dtf = DatatypeFactory.newInstance();
      XMLGregorianCalendar xgc = dtf.newXMLGregorianCalendar();
      Calendar cal = Calendar.getInstance();
      xgc.setYear(cal.get(Calendar.YEAR));
      xgc.setDay(cal.get(Calendar.DAY_OF_MONTH));
      xgc.setMonth(cal.get(Calendar.MONTH) + 1);
      xgc.setHour(cal.get(Calendar.HOUR_OF_DAY));
      xgc.setMinute(cal.get(Calendar.MINUTE));
      xgc.setSecond(cal.get(Calendar.SECOND));
      xgc.setMillisecond(cal.get(Calendar.MILLISECOND));
      return xgc;
    } catch (DatatypeConfigurationException e) {
      return null;
    }
  }

}
