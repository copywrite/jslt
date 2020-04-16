package com.schibsted.spt.data.jslt.json;

/**
 * JSON null value.
 *
 * Do we need a class for null, or just use java null?
 * Singleton?
 */
public class JsonNull extends JsonValue {

    // TODO: Rename to NULL
    public static final JsonNull instance = new JsonNull();

    private JsonNull() {}

    @Override
    public boolean isNull() { return true; }
}
