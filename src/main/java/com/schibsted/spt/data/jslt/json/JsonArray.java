package com.schibsted.spt.data.jslt.json;

import java.util.ArrayList;

/**
 * A JSON array.
 */
public class JsonArray extends JsonValue {

    private ArrayList<JsonValue> array = new ArrayList<JsonValue>();

    @Override
    public boolean isArray() { return true; }

    @Override
    public int size() { return array.size(); }

    public void add(JsonValue value) { array.add(value); }

    public void addAll(JsonArray other) { array.addAll(other.array); }

    // Do we need this, or should we only allow add(new JsonString(value))?
    public void add(String value) { add(new JsonString(value)); }

    @Override
    public JsonValue get(int index) {
        if (index >= 0 && index < array.size())
            return array.get(index);

        return null;
    }

    @Override
    public String toString()
    {
        if (array.size() == 0) return "[]";

        String str=array.get(0).toString();
        for(int i=1;i<array.size();i++) {
            str+=","+array.get(i);
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
