package com.schibsted.spt.data.jslt.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A JSON object.
 *
 * Should the order of fields be retained?
 */
public class JsonObject extends JsonValue /* implements Map<String, JsonValue> */ {

    // TODO: Have to use LinkedHashMap to retain key order. Otherwise some tests don't pass.
    private Map<String, JsonValue> map = new LinkedHashMap<String, JsonValue>();

    public boolean isObject() { return true; }

    public int size() { return map.size(); }

    public Iterator<Map.Entry<String, JsonValue>> fields() {
        return map.entrySet().iterator();
    }

    public JsonValue get(String key) { return map.get(key); }

    public void put(String key, JsonValue value) {
        map.put(key, value);
    }

    public void putAll(JsonObject other) {
        map.putAll(other.map);
    }

    // Should we have putters for different values?
    public void put(String key, String value) { put(key, new JsonString(value));}

    public boolean has(String key) {
        return map.containsKey(key);
    }

    @Override
    public String toString() {
        Stream<String> body = map.entrySet().stream().map(e -> "\"" + JsonString.escape(e.getKey()) + "\" : " + e.getValue());
        return "{" + String.join(", ", body.collect(Collectors.toList())) + "}";
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o instanceof JsonObject) {
            return ((JsonObject) o).map.equals(map);
        }
        return false;
    }

    @Override
    public int hashCode() { return map.hashCode(); }
}
