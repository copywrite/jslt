
package com.schibsted.spt.data.jslt;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.io.IOException;

import com.schibsted.spt.data.jslt.json.JsonUtils;
import com.schibsted.spt.data.jslt.json.JsonValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.junit.Test;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * JSON parsing test cases. Verifies that Jackson and JSLT produce the
 * same JSON structure.
 */
@RunWith(Parameterized.class)
public class JsonParseTest {
  private String json;

  public JsonParseTest(String json) {
    this.json = json;
  }

  @Test
  public void check() {
    try {
      Expression expr = Parser.compileString(json);
      JsonValue actual = expr.apply(null);

      JsonValue expected = JsonUtils.fromJson(json);

      assertEquals("actual class " + actual.getClass() + ", expected class " + expected.getClass(), expected, actual);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (JsltException e) {
      throw new RuntimeException("Parsing '" + json + "' failed", e);
    }
  }

  @Parameters
  public static Collection<Object[]> data() {
    JsonNode json = TestUtils.loadFile("json-parse-tests.json");
    JsonNode tests = json.get("tests");

    List<Object[]> strings = new ArrayList();
    for (int ix = 0; ix < tests.size(); ix++)
      strings.add(new Object[] { tests.get(ix).asText() });
    return strings;
  }
}
