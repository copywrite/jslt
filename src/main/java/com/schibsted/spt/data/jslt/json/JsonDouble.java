package com.schibsted.spt.data.jslt.json;

/**
 * A JSON number represented as a 64-bit floating point.
 */
public class JsonDouble extends JsonNumber {

    private double value;

    public JsonDouble(double value) {
        this.value = value;
    }

    //FIXME: We should probably do something about NaNs.
    @Override
    public String toString() { return String.valueOf(value); }

    @Override
    public double doubleValue() { return (double) value; }

    @Override
    public long longValue() { return (long) value; }

    @Override
    public int intValue() { return (int) value; }

    @Override
    public boolean isFloatingPointNumber() { return true; }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o instanceof JsonDouble) {
            return ((JsonDouble) o).value == value;
        }
        return false;
    }

    @Override
    public int hashCode() { return Double.valueOf(value).hashCode(); }
}
