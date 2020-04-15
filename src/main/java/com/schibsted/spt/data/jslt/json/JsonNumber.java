package com.schibsted.spt.data.jslt.json;

/**
 * A class representing JSON number.
 *
 * JSON only defines a syntax for numbers, not semantics.
 * JSON numbers are essentially arbitrary-precision decimal floating point numbers,
 * but it might not be desirable to represent JSON numbers as java.math.BigDecimal for performance reasons.
 * JSLT is agnostic to the number representation in Jackson NumberNode, but converts values to either long or double
 * for numerical operations.
 *
 * For engineering tasks, double precision floating point is usually sufficient for all purposes.
 *
 */
public class JsonNumber extends JsonValue {
}
