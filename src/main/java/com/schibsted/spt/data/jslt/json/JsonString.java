package com.schibsted.spt.data.jslt.json;

import java.io.IOException;

/**
 * A JSON string value.
 */
public class JsonString extends JsonValue {

    private String value;

    public JsonString(String value) { this.value = value; }

    @Override
    public String stringValue() { return value; }

    @Override
    public boolean isString() { return true; }

    @Override
    public String toString()
    {
        return "\""+escape(value)+"\"";
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o instanceof JsonString) {
            return ((JsonString) o).value.equals(value);
        }
        return false;
    }

    @Override
    public int hashCode() { return value.hashCode(); }

    /**
     * Encode a string as a JSON string, escaping characters as needed.
     * @param string String to encode.
     * @return Encoded string.
     */
    public static String escape(String string)
    {
        //TODO: Implement!
        return string;
    }
}
