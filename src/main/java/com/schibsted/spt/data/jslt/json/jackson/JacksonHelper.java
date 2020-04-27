package com.schibsted.spt.data.jslt.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.schibsted.spt.data.jslt.JsltException;
import com.schibsted.spt.data.jslt.json.JsonInt;
import com.schibsted.spt.data.jslt.json.JsonNull;
import com.schibsted.spt.data.jslt.json.JsonValue;

import java.io.IOException;

public class JacksonHelper {

    private static ObjectMapper mapper = new ObjectMapper();

    private static ObjectMapper mapper2 = new ObjectMapper()
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            ;
    private static ObjectWriter writer = mapper2.writer();

    public static JsonValue fromJson(String input) throws IOException {
        JsonNode jackson = mapper.readTree(input);
        return JacksonConverter.fromJackson(jackson);
    }

    public static String toJson(JsonValue value) throws IOException {
        JsonNode jackson = JacksonConverter.toJackson(value);
        return mapper.writeValueAsString(jackson);
    }

    public static int hashCode(JsonValue value)
    {
        JsonNode node = JacksonConverter.toJackson(value);
        try {
            // https://stackoverflow.com/a/18993481/90580
            final Object obj = mapper.treeToValue(node, Object.class);
            String jsonString = writer.writeValueAsString(obj);
            return jsonString.hashCode();
        } catch (JsonProcessingException e) {
            throw new JsltException("hash-int: can't process json" + e);
        }
    }
}
