package com.schibsted.spt.data.jslt.json;

import java.io.IOException;

public class JsonUtils {

    // JsonNode.asText()
    // This function is only needed once.
    public static String asText(JsonValue value) {
        throw new UnsupportedOperationException();
    }

    // JsonNode.asDouble()
    // This function is only used once and probably not needed even there.
    public static double asDouble(JsonValue value) {
        throw new UnsupportedOperationException();
    }

    // Parse JSON string.
    // Should we throw, or just return null?
    public static JsonValue fromJson(String json) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    // Produce JSON string.
    // Not sure why this should ever throw an exception.
    public static String toJson(JsonValue value) throws IOException
    {
        throw new UnsupportedOperationException();
    }
}
