/**
 * Package for a minimalistic JSON data structure representation.
 *
 * Design principles:
 * 1. Concrete classes with public constructors.
 *    Pros: Avoid factories
 *    Cons: Interfaces would allow different implementations depending on use-cases.
 *
 * 2. Immutability (not feasible)
 *    Pros: The usual ones. Low-cost thread safety.
 *    Cons: Java 8 doesn't have immutable maps and lists! Probably much trouble to create and
 *          maintain high-performance implementations just for JSLT. Need to investigate!
 *          Potential performance issues. Additional complexity in manipulating arrays and objects.
 *
 * 3. Minimalism
 *    Pros: Simplistic and concise design doesn't require frequent updates and retains backwards compatibility.
 *    Cons: No helper methods in classes. Java doesn't have extension methods.
 */
package com.schibsted.spt.data.jslt.json;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Base class for JSON values.
 *
 */
public abstract class JsonValue {

    /* Should we have these?
     * - Could use instanceof instead.
     * - Could be implemented with instanceof.
     * - Overriding in subclasses may offer a slight performance improvement.
     */
    public boolean isNull() { return false; }
    public boolean isBoolean() { return false; }
    public boolean isString() { return false; }
    public boolean isNumber() { return false; }
    public boolean isObject() { return false; }
    public boolean isArray() { return false; }

    // Return length of array or number of elements in an object.
    public int size() { return 0; }

    // Should this really be here, or just in JsonObject?
    public JsonValue get(String key) { return null; }

    // Should this be only in JsonArray?
    public JsonValue get(int index) { return null; }

    // Questionable. Should we require casting to subclass to get access?
    public int intValue() { return (int)longValue(); }
    public long longValue() { return (long)doubleValue(); }
    public double doubleValue() { return 0; }

    public boolean booleanValue() { return false; }

    // Is null the right default value?
    public String stringValue() { return null; }

    // Got to think about these.
    public boolean isIntegralNumber() { return false; }
    public boolean isFloatingPointNumber() { return false; }

    // ??
    public boolean has() { return false; }

    // Really?
    public Iterator<Map.Entry<String, JsonValue>> fields() {
        return Collections.emptyIterator();
    }

    // Needs to be implemented in a proper way. Used by NodeUtils.toString().
    public String toString() { throw new UnsupportedOperationException(); }
}
