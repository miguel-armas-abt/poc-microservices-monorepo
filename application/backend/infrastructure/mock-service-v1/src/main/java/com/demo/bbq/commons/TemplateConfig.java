package com.demo.bbq.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TemplateConfig {
    public static final String readJSON(String filename) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(filename);

        String data;
        try {
            byte[] bdata = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            data = new String(bdata, StandardCharsets.UTF_8);
        } catch (IOException var5) {
            throw var5;
        }

        ObjectMapper mapper = new ObjectMapper();
        Object jsonObject = mapper.readValue(data, Object.class);
        return mapper.writeValueAsString(jsonObject);
    }
}
