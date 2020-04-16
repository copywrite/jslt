package com.schibsted.spt.data.jslt.json;

/**
 * A JSON number represented as a 64-bit floating point.
 */
public class JsonDouble extends JsonNumber {

    private double value_;

    public JsonDouble(double value) {
        value_ = value;
    }
}
