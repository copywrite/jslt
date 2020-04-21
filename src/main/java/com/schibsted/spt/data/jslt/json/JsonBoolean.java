package com.schibsted.spt.data.jslt.json;

import com.schibsted.spt.data.jslt.impl.BuiltinFunctions;

/**
 * JSON 'true' or 'false'.
 *
 * Only two instances of this class are ever actually needed.
 * If JsonNull is singleton, perhaps there should be singleton subclasses, JsonTrue & JsonFalse?
 */
final public class JsonBoolean extends JsonValue {


    private final boolean value;

    public static final JsonBoolean FALSE = new JsonBoolean(false);
    public static final JsonValue TRUE = new JsonBoolean(true);

    private JsonBoolean(boolean value)
    {
        this.value = value;
    }

    @Override
    public boolean isBoolean() { return true; }

    @Override
    public boolean booleanValue() { return value; }

    public static JsonValue valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public String toString()
    {
        return value ? "true" : "false";
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o instanceof JsonBoolean) {
            return ((JsonBoolean) o).value == value;
        }
        return false;
    }

    @Override
    public int hashCode() { return Boolean.valueOf(value).hashCode(); }
}
