package com.demo.bbq.commons.files;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JsonFileReaderUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> T getAnElement(String filePath, Class<T> classSource) {
    try {
      return objectMapper.readValue(JsonFileReaderUtil.class.getClassLoader().getResourceAsStream(filePath), classSource);
    } catch (IOException ioException) {
      throw new SystemException("ErrorGettingElement", "Error getting a JSON element");
    }
  }

  public static <T> List<T> getList(String filePath, Class<T[]> classSource) {
    try {
      T[] array = objectMapper.readValue(Objects.requireNonNull(JsonFileReaderUtil.class.getClassLoader().getResourceAsStream(filePath)), classSource);
      return Arrays.asList(array);
    } catch (IOException ioException) {
      throw new SystemException("ErrorGettingList", "Error getting a JSON list");
    }
  }
}