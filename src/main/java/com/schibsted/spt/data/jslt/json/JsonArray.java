package com.schibsted.spt.data.jslt.json;

import java.util.ArrayList;

/**
 * A JSON array.
 */
public class JsonArray extends JsonValue {

    private ArrayList<JsonValue> array_ = new ArrayList<JsonValue>();

    public void add(JsonValue value) { array_.add(value); }

    public void addAll(JsonArray other) { throw new UnsupportedOperationException(); }

    // Do we need this?
    public void add(String value) { add(new JsonString(value)); }
}
