package com.schibsted.spt.data.jslt.json;

/**
 * JSON 'true' or 'false'.
 *
 * Only two instances of this class are ever actually needed.
 * If JsonNull is singleton, perhaps there should be singleton subclasses, JsonTrue & JsonFalse?
 */
final public class JsonBoolean extends JsonValue {


    private final boolean value_;

    public static final JsonBoolean FALSE = new JsonBoolean(false);
    public static final JsonValue TRUE = new JsonBoolean(true);

    private JsonBoolean(boolean value)
    {
        value_ = value;
    }
}
