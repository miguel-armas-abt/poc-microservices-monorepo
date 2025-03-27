package com.demo.poc.entrypoint.rules.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DroolFileGeneratorApplication {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String SOURCE         = "source";
  private final String XLS_EXTENSION  = ".xls";
  private final String DRL_EXTENSION  = ".drl";

  public static void main(String[] args) {

    DroolFileGeneratorApplication xlsToDrlUtil = new DroolFileGeneratorApplication();

    xlsToDrlUtil.getDLR("C:\\dev-workspace\\poc-microservices-monorepo\\application\\backend\\business\\rules-processor-v1\\src\\main\\resources\\rules\\",
        "discount_rules");
  }
  public  void getDLR(String basePath, String nameRule) {

    try {
      InputStream is = new FileInputStream(basePath.concat(SOURCE).concat(File.separator).concat(nameRule).concat(XLS_EXTENSION));

      // Create compiler class instance
      SpreadsheetCompiler sc = new SpreadsheetCompiler();

      // Compile the excel to generate the (.drl) file
      StringBuilder drl = new StringBuilder(sc.compile(is, InputType.XLS));

      // Insert dialect value into drl file
      File outputFile = new File(basePath.concat(nameRule).concat(DRL_EXTENSION));

      if (outputFile.exists()) {
        outputFile.delete();
      }

      FileUtils.write(outputFile, drl, StandardCharsets.UTF_8);

      System.out.println("Drl generated: " + nameRule.concat(DRL_EXTENSION));
    } catch (IOException e) {
      logger.error("Error on getDLR method",e);
    }
  }
}
