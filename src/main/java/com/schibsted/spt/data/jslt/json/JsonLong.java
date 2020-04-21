package com.schibsted.spt.data.jslt.json;

// Is this class a good idea?
public class JsonLong extends JsonNumber {

    private long value;

    public JsonLong(long value) {
        this.value = value;
    }

    @Override
    public String toString() { return String.valueOf(value); }

    @Override
    public double doubleValue() { return (double) value; }

    @Override
    public long longValue() { return (long) value; }

    @Override
    public int intValue() { return (int) value; }

    @Override
    public boolean isIntegralNumber() { return true; }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o instanceof JsonLong) {
            return ((JsonLong) o).value == value;
        }
        return false;
    }

    @Override
    public int hashCode() { return Long.valueOf(value).hashCode(); }
}