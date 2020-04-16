package com.schibsted.spt.data.jslt.json;

/**
 * JSON null value.
 *
 * Do we need a class for null, or just use java null?
 * Singleton?
 */
public class JsonNull extends JsonValue {

    public static final JsonNull instance = new JsonNull();

    private JsonNull() {}
}
