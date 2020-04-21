package com.schibsted.spt.data.jslt.json;

import com.schibsted.spt.data.jslt.json.jackson.JacksonHelper;

import java.io.IOException;

public class JsonUtils {



    // JsonNode.asText()
    // This function is only needed once.
    public static String asText(JsonValue value) {
        throw new UnsupportedOperationException("asText() not implemented!");
    }

    // JsonNode.asDouble()
    // This function is only used once and probably not needed even there.
    public static double asDouble(JsonValue value) {
        throw new UnsupportedOperationException("asDouble not implemented!");
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
