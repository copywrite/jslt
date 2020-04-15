/**
 * Package for a minimalistic JSON data structure representation.
 *
 * Design principles:
 * 1. Concrete classes with public constructors.
 *    Pros: Avoid factories
 *    Cons: Interfaces would allow different implementations depending on use-cases.
 *
 * 2. Immutability
 *    Pros: The usual ones. Low-cost thread safety.
 *    Cons: Potential performance issues. Additional complexity in manipulating arrays and objects.
 *
 * 3. Minimalism
 *    Pros: Simplistic and concise design doesn't require frequent updates and retains backwards compatibility.
 *    Cons: No helper methods in classes. Java doesn't have extension methods.
 */
package com.schibsted.spt.data.jslt.json;

/**
 * Base class for JSON values.
 *
 */
public abstract class JsonValue {
}
