package com.schibsted.spt.data.jslt.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schibsted.spt.data.jslt.json.JsonValue;

import java.io.IOException;

public class JacksonHelper {

    private static ObjectMapper mapper = new ObjectMapper();

    public static JsonValue fromJson(String input) throws IOException {
        JsonNode jackson = mapper.readTree(input);
        return JacksonConverter.fromJackson(jackson);
    }

    public static String toJson(JsonValue value) throws IOException {
        JsonNode jackson = JacksonConverter.toJackson(value);
        return mapper.writeValueAsString(jackson);
    }
}
