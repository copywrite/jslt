package com.schibsted.spt.data.jslt.json;

// Is this class a good idea?
public class JsonLong extends JsonNumber {

    private long value_;

    public JsonLong(long value) {
        value_ = value;
    }
}