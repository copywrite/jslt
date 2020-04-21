package com.schibsted.spt.data.jslt.json;

/**
 * JSON null value.
 *
 * Do we need a class for null, or just use java null?
 * Singleton?
 */
public class JsonNull extends JsonValue {

    public static final JsonNull NULL = new JsonNull();

    private JsonNull() {}

    @Override
    public boolean isNull() { return true; }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        return (o instanceof JsonNull);
    }

    @Override
    public int hashCode() { return 0; }
}
