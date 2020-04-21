
package com.schibsted.spt.data.jslt;

import java.util.Collection;
import java.util.Collections;

import com.schibsted.spt.data.jslt.json.JsonUtils;
import com.schibsted.spt.data.jslt.json.JsonValue;
import org.junit.Test;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Test cases for the function wrapper implementations.
 */
public class FunctionWrapperTest extends TestBase {

  @Test
  public void testWrapStaticMethod() throws Exception {
    Collection<Function> functions = Collections.singleton(
      FunctionUtils.wrapStaticMethod("url-decode",
                                     "java.net.URLDecoder", "decode",
                                     new Class[] {String.class, String.class})
    );

    check("{}", "url-decode(\"foo\", \"utf-8\")", "\"foo\"",
          Collections.EMPTY_MAP,
          functions);
  }

  @Test
  public void testWrapStaticMethodLong() throws Exception {
    Collection<Function> functions = Collections.singleton(
      FunctionUtils.wrapStaticMethod("time-millis",
                                     "java.lang.System", "currentTimeMillis")
    );
    String query = "time-millis()";

    long before = System.currentTimeMillis();

    JsonValue context = JsonUtils.fromJson("{}");
    Expression expr = Parser.compileString(query, functions);
    JsonValue actual = expr.apply(context);
    long value = actual.longValue();

    long after = System.currentTimeMillis();

    assertTrue(before <= value);
    assertTrue(value <= after);
  }

  @Test
  public void testWrapStaticMethodNumeric() throws Exception {
    Collection<Function> functions = Collections.singleton(
      FunctionUtils.wrapStaticMethod("pow",
                                     "java.lang.Math", "pow")
    );
    String query = "pow(2, 10)";

    JsonValue context = JsonUtils.fromJson("{}");
    Expression expr = Parser.compileString(query, functions);
    JsonValue actual = expr.apply(context);

    assertTrue(actual.longValue() == 1024);
  }
}
