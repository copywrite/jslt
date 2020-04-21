
package com.schibsted.spt.data.jslt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.schibsted.spt.data.jslt.json.JsonInt;
import com.schibsted.spt.data.jslt.json.JsonValue;

public class TestFunction implements Function {

  public String getName() {
    return "test";
  }

  public int getMinArguments() {
    return 0;
  }

  public int getMaxArguments() {
    return 0;
  }

  public JsonValue call(JsonValue input, JsonValue[] params) {
    return new JsonInt(42);
  }
}
