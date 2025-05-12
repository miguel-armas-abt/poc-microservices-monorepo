package com.demo.poc.utils;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class YamlReader {

  public static <T> T read(String fileName, Class<T> modelClass) {
    InputStream inputStream = YamlReader.class.getClassLoader().getResourceAsStream(fileName);
    return initYaml().loadAs(inputStream, modelClass);
  }

  public static void create(String absolutePath, Map<String, Object> yamlData) {
    try {
      File file = new File(absolutePath);
      if (file.exists() && !file.canWrite())
        throw new IOException("Cannot write to file: " + file.getAbsolutePath());

      Optional.ofNullable(file.getParentFile())
          .filter(parentDir -> !parentDir.exists())
          .ifPresent(parentDir -> Optional.of(parentDir.mkdirs())
              .filter(isCreatedFolder -> isCreatedFolder)
              .orElseThrow(() -> new RuntimeException("Cannot create folder")));

      try (FileWriter writer = new FileWriter(file)) {
        initYaml().dump(yamlData, writer);
        log.info("File created: {}", file.getAbsolutePath());
      }

    } catch (IOException exception) {
      throw new RuntimeException("Error creating file " + absolutePath, exception);

    }
  }

  private static Yaml initYaml() {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

    return new Yaml(options);
  }
}
