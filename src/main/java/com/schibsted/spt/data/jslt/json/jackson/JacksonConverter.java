package com.schibsted.spt.data.jslt.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.schibsted.spt.data.jslt.json.*;

import java.util.Iterator;
import java.util.Map;

public class JacksonConverter {
    private static ObjectMapper mapper = new ObjectMapper();

    public static JsonNode toJackson(JsonValue value) {
        if (value.isString()) return new TextNode(value.stringValue());

        // Numbers are a bit weird.
        if (value instanceof JsonInt) return new IntNode(value.intValue());
        if (value.isNumber() && value.isIntegralNumber()) return new LongNode(value.longValue());
        if (value.isNumber()) return new DoubleNode(value.doubleValue());

        if (value.isBoolean()) return BooleanNode.valueOf(value.booleanValue());
        if (value.isNull()) return NullNode.instance;

        if (value.isArray()) {
            ArrayNode array = mapper.createArrayNode();
            for (int i = 0; i < value.size(); i++) {
                array.add(toJackson(value.get(i)));
            }
            return array;
        }

        if (value.isObject()) {
            ObjectNode object = mapper.createObjectNode();

            for (Iterator<String> it = value.fieldNames(); it.hasNext(); ) {
                String key = it.next();

                object.set(key, toJackson(value.get(key)));
            }
            return object;
        }

        throw new UnsupportedOperationException("This shouldn't happen. Bad type "+value.getClass());
    }

    private static JsonValue fromJacksonArray(ArrayNode node) {
        JsonArray array = new JsonArray();
        for(JsonNode e : node) {
            array.add(fromJackson(e));
        }
        return array;
    }

    private static JsonValue fromJacksonObject(ObjectNode node) {
        JsonObject object = new JsonObject();
        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> e = it.next();

            object.put(e.getKey(), fromJackson(e.getValue()));
        }
        return object;
    }

    public static JsonValue fromJackson(JsonNode node) {
        switch (node.getNodeType()) {
            case ARRAY:
                return fromJacksonArray((ArrayNode)node);

            case OBJECT:
                return fromJacksonObject((ObjectNode)node);

            case BOOLEAN:
                return JsonBoolean.valueOf(node.booleanValue());

            case NULL:
                return JsonNull.NULL;

            case NUMBER:
                if (node.isFloatingPointNumber())
                    return new JsonDouble(node.doubleValue());
                else if (node.isLong())
                    return new JsonLong(node.longValue());
                else
                    return new JsonInt(node.intValue());

            case STRING:
                return new JsonString(node.textValue());
            default:
                return null;
        }
    }
}
