package com.schibsted.spt.data.jslt.json;

import java.util.ArrayList;

/**
 * A JSON array.
 */
public class JsonArray extends JsonValue {

    private ArrayList<JsonValue> array = new ArrayList<JsonValue>();

    public boolean isArray() { return true; }

    public int size() { return array.size(); }

    public void add(JsonValue value) { array.add(value); }

    public void addAll(JsonArray other) { throw new UnsupportedOperationException(); }

    // Do we need this, or should we only allow add(new JsonString(value))?
    public void add(String value) { add(new JsonString(value)); }

    public JsonValue get(int index) { return array.get(index); }

    @Override
    public String toString()
    {
        if (array.size() == 0) return "[]";

        String str=array.get(0).toString();
        for(int i=1;i<array.size();i++) {
            str+=", "+array.get(i);
        }

        return "["+str+"]";
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o instanceof JsonArray) {
            return ((JsonArray) o).array.equals(array);
        }
        return false;
    }

    @Override
    public int hashCode() { return array.hashCode(); }
}
