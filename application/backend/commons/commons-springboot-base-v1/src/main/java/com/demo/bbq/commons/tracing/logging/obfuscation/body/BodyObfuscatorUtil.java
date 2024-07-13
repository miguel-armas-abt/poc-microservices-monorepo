package com.demo.bbq.commons.tracing.logging.obfuscation.body;

import static com.demo.bbq.commons.tracing.logging.obfuscation.constants.ObfuscationConstant.JSON_SPLITTER_REGEX;
import static com.demo.bbq.commons.tracing.logging.obfuscation.constants.ObfuscationConstant.ARRAY_WILDCARD;
import static com.demo.bbq.commons.tracing.logging.obfuscation.constants.ObfuscationConstant.OBFUSCATION_MASK;

import com.demo.bbq.commons.properties.dto.obfuscation.ObfuscationTemplate;
import com.demo.bbq.commons.errors.exceptions.SystemException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class BodyObfuscatorUtil {


    /**
     * Example obfuscation.bodyFields: ["user.password", "user.phones[*]"]
     */
    public static String process(ObfuscationTemplate obfuscation, String value) {
        try {
            if (StringUtils.isEmpty(value)) {
                return value;
            }

            Set<String> bodyFields = Optional
                .ofNullable(obfuscation.getBodyFields())
                .orElse(Set.of());

            var jsonObject = new JSONObject(value);

            bodyFields.forEach(fieldPath -> {
                String[] fieldPathSegments = fieldPath.split(JSON_SPLITTER_REGEX);
                int incrementalIndex = 0;
                obfuscateFieldRecursively(jsonObject, fieldPathSegments, incrementalIndex);
            });

            return jsonObject.toString();

        } catch (Exception e) {
            return value;
        }
    }

    /**
     * Example arrayKey: If the segment is equal to "phones[*]" then the arrayKey is "phones"
     */
    private static void obfuscateFieldRecursively(JSONObject jsonObject, String[] fieldPathSegments, int index) {
        if (wereAllFieldsProcessed(fieldPathSegments, index)) return;

        String segment = fieldPathSegments[index];

        if (segment.contains(ARRAY_WILDCARD)) {
            String arrayKey = segment.substring(0, segment.indexOf('['));
            JSONArray jsonArray = jsonObject.optJSONArray(arrayKey);
            processArray(jsonArray, fieldPathSegments, index);
        } else {
            processObject(jsonObject, segment, fieldPathSegments, index);
        }
    }

    private static boolean wereAllFieldsProcessed(String[] fieldPathSegments, int index) {
        return index >= fieldPathSegments.length;
    }

    private static void processArray(JSONArray jsonArray, String[] fieldPathSegments, int index) {
        Optional.ofNullable(jsonArray)
            .ifPresent(array -> IntStream.range(0, array.length())
                .mapToObj(array::optJSONObject)
                .filter(Objects::nonNull)
                .forEach(jsonObject -> obfuscateFieldRecursively(jsonObject, fieldPathSegments, index + 1)));
    }

    private static void processObject(JSONObject jsonObject, String segment, String[] fieldPathSegments, int index) {
        if (wasLastSegment(fieldPathSegments, index)) {
            obfuscateTargetField(jsonObject, segment);
        } else {
            Optional.ofNullable(jsonObject.optJSONObject(segment))
                .ifPresent(nextJsonObject -> obfuscateFieldRecursively(nextJsonObject, fieldPathSegments, index + 1));
        }
    }

    private static boolean wasLastSegment(String[] fieldPathSegments, int index) {
        return index == fieldPathSegments.length - 1;
    }

    private static void obfuscateTargetField(JSONObject jsonTarget, String field) {
        Optional.ofNullable(jsonTarget.optString(field, null))
            .ifPresent(originalValue -> {
                try {
                    jsonTarget.put(field, partiallyObfuscate(originalValue));
                } catch (JSONException ex) {
                    throw new SystemException("ErrorObfuscating", ex.getMessage());
                }
            });
    }

    private static String partiallyObfuscate(String value) {
        return value.length() > 6
            ? value.substring(0, 3) + OBFUSCATION_MASK + value.substring(value.length() - 3)
            : value;
    }
}