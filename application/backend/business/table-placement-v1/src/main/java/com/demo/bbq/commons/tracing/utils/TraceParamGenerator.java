package com.demo.bbq.commons.tracing.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.function.UnaryOperator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TraceParamGenerator {

  public static final String DEFAULT_DATE_PATTERN = "yyyyMMddHHmmssSSS";
  public static final int PARENT_ID_SIZE = 16;
  public static final int TRACE_ID_SIZE = 32;

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

  public static String getTrace(int size) {
    StringBuilder traceState = new StringBuilder();
    SecureRandom random = new SecureRandom();
    byte[] randomBytes = new byte[(size / 2)];
    random.nextBytes(randomBytes);
    for (byte randomByte : randomBytes) {
      String hex = Integer.toHexString(randomByte + 128);
      if (hex.length() == 1) {
        hex = "0" + hex;
      }
      traceState.append(hex);
    }
    return traceState.toString();
  }

}
