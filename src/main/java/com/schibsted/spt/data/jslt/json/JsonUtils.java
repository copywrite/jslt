package com.schibsted.spt.data.jslt.json;

import com.schibsted.spt.data.jslt.json.jackson.JacksonHelper;

import java.io.IOException;

public class JsonUtils {



    // JsonNode.asText()
    // This function is only needed once.
    public static String asText(JsonValue value) {
        if (value.isString()) return value.stringValue();
        if (value.isIntegralNumber()) return String.valueOf(value.longValue());
        if (value.isFloatingPointNumber()) return String.valueOf(value.doubleValue());
        if (value.isBoolean()) return value.toString();
        if (value.isNull()) return value.toString();
        return "";
    }

    // JsonNode.asDouble()
    // This function is only used once and probably not needed even there.
    public static double asDouble(JsonValue value) {
        if (value.isString()) return Double.valueOf(value.stringValue());
        if (value.isNumber()) return value.doubleValue();
        return 0;
    }

    // Parse JSON string.
    // Should we throw, or just return null?
    public static JsonValue fromJson(String json) throws IOException
    {
        return JacksonHelper.fromJson(json);
    }

    // Produce JSON string.
    // Not sure why this should ever throw an exception.
    public static String toJson(JsonValue value) throws IOException
    {
        return JacksonHelper.toJson(value);
    }
}
