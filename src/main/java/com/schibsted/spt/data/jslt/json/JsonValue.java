/**
 * Package for a minimalistic JSON data structure representation.
 *
 * Design principles:
 * 1. Concrete classes with public constructors. (Conflicts with 4.)
 *    Pros: Avoid factories. (Not really an issue with immutable types).
 *    Cons: Interfaces would allow different implementations depending on use-cases.
 *
 * 2. Immutability (not feasible?)
 *    Pros: The usual ones. Low-cost thread safety.
 *    Cons: Java 8 doesn't have immutable maps and lists! Probably much trouble to create and
 *          maintain high-performance implementations just for JSLT. Need to investigate!
 *          Potential performance issues. Additional complexity in manipulating arrays and objects.
 *
 * 3. Minimalism
 *    Pros: Simplistic and concise design doesn't require frequent updates and retains backwards compatibility.
 *    Cons: No helper methods in classes. Java doesn't have extension methods.
 * 
 * 4. Interfaces or abstract classes.
 *    This allows multiple different implementation with possibility
 *    of performance optimizations in certain cases.
 * 
 * 5. The JSON library must be usable by itself.
 *    Otherwise even trivial use cases will often require reverting to another JSON library.
 *    However, the library doesn't have to compete with others.
 *    a. Client can construct JSON values. Especially if the values are immutable,
 *       the client must be able to constuct JSON values. 
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
    // We definitely shouldn't allow narrowing conversions without thorough checking.
    public int intValue() { return 0; }
    public long longValue() { return 0; }
    public double doubleValue() { return 0; }

    public boolean booleanValue() { return false; }

    // Is null the right default value?
    public String stringValue() { return null; }

    // Got to think about these.
    public boolean isIntegralNumber() { return false; }
    public boolean isFloatingPointNumber() { return false; }

    // ??
    public boolean has(String key) { return false; }

    // Really?
    public Iterator<Map.Entry<String, JsonValue>> fields() {
        return Collections.emptyIterator();
    }

    @Override
    abstract public String toString();

    @Override
    abstract public boolean equals(Object o);

    @Override
    abstract public int hashCode();

    // FIXME: This is only used in one test. Get rid of it.
    public Iterator<String> fieldNames() {
        final Iterator<Map.Entry<String, JsonValue>> fields = this.fields();
        return new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return fields.hasNext();
            }

            @Override
            public String next() {
                return fields.next().getKey();
            }
        };
    }
}
