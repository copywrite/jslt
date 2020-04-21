package com.schibsted.spt.data.jslt.json;

// We most likely do not need this.
public class JsonInt extends JsonNumber {

    private int value;

    public JsonInt(int value) { this.value = value; }

    @Override
    public String toString() { return ""+ value; }

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
        if (o instanceof JsonInt) {
            return ((JsonInt) o).value == value;
        }
        return false;
    }

    @Override
    public int hashCode() { return Integer.valueOf(value).hashCode(); }
}
