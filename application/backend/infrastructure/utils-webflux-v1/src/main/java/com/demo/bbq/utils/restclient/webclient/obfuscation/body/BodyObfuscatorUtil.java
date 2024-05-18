package com.demo.bbq.utils.restclient.webclient.obfuscation.body;

import static com.demo.bbq.utils.restclient.webclient.obfuscation.constants.ObfuscationConstant.WILD_CARD;

import com.demo.bbq.utils.errors.exceptions.SystemException;
import com.demo.bbq.utils.properties.dto.ObfuscationTemplate;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class BodyObfuscatorUtil {

    public static String process(ObfuscationTemplate obfuscation, String value) {
        try {
            if (StringUtils.isEmpty(value)) {
                return value;
            }
            Set<String> sensitiveFields = obfuscation.getBodyFields();
            if (sensitiveFields == null || sensitiveFields.isEmpty()) {
                return value;
            }

            var jsonObject = new JSONObject(value);
            sensitiveFields.forEach(fieldPath -> obfuscateField(jsonObject, fieldPath));
            return jsonObject.toString();
        } catch (Exception e) {
            return value;
        }
    }

    private static void obfuscateField(JSONObject jsonObject, String fieldPath) {
        String[] parts = fieldPath.split("\\.");
        obfuscateFieldRecursively(jsonObject, parts, 0);
    }

    private static void obfuscateFieldRecursively(JSONObject jsonObject, String[] parts, int index) {
        if (index >= parts.length) return;

        String part = parts[index];
        if (isWildcardPart(part)) {
            processArray(jsonObject, part, parts, index);
        } else {
            processObject(jsonObject, part, parts, index);
        }
    }

    private static boolean isWildcardPart(String part) {
        return part.contains(WILD_CARD);
    }

    private static void processArray(JSONObject jsonObject, String part, String[] parts, int index) {
        String arrayKey = part.substring(0, part.indexOf('['));
        JSONArray jsonArray = jsonObject.optJSONArray(arrayKey);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject arrayItem = jsonArray.optJSONObject(i);
                if (arrayItem != null) {
                    obfuscateFieldRecursively(arrayItem, parts, index + 1);
                }
            }
        }
    }

    private static void processObject(JSONObject jsonObject, String part, String[] parts, int index) {
        if (index == parts.length - 1) {
            obfuscateTargetField(jsonObject, part);
        } else {
            JSONObject nextJsonObject = jsonObject.optJSONObject(part);
            if (nextJsonObject != null) {
                obfuscateFieldRecursively(nextJsonObject, parts, index + 1);
            }
        }
    }

    private static void obfuscateTargetField(JSONObject targetObject, String field) {
        if (targetObject.has(field)) {
            String originalValue = targetObject.optString(field);
            try {
                targetObject.put(field, partiallyObfuscate(originalValue));
            } catch (JSONException e) {
                throw new SystemException("ErrorObfuscating");
            }
        }
    }

    private static String partiallyObfuscate(String value) {
        return value.length() > 6 ? value.substring(0, 3) + "****" + value.substring(value.length() - 3) : value;
    }
}
