package com.schibsted.spt.data.jslt.json;

import java.util.Iterator;
import java.util.Map;

/**
 * A JSON object.
 *
 * Should the order of fields be retained?
 */
public class JsonObject extends JsonValue /* implements Map<String, JsonValue> */ {

    public Iterator<Map.Entry<String, JsonValue>> fields() {
        throw new UnsupportedOperationException();
    }

    public void put(String key, JsonValue value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(JsonObject other) { throw new UnsupportedOperationException(); }

    // Should we have putters for different values?
    public void put(String key, String value) { put(key, new JsonString(value));}

    public boolean has(String key) {
        throw new UnsupportedOperationException();
    }
}
