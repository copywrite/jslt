
// Copyright 2018 Schibsted Marketplaces Products & Technology As
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.schibsted.spt.data.jslt.impl;


import com.schibsted.spt.data.jslt.json.JsonArray;
import com.schibsted.spt.data.jslt.json.JsonObject;
import com.schibsted.spt.data.jslt.json.JsonString;
import com.schibsted.spt.data.jslt.json.JsonValue;

public class PlusOperator extends NumericOperator {

  public PlusOperator(ExpressionNode left, ExpressionNode right,
                      Location location) {
    super(left, right, "+", location);
  }

  public JsonValue perform(JsonValue v1, JsonValue v2) {
    if (v1.isString() || v2.isString()) {
      // if one operand is string: do string concatenation
      return new JsonString(NodeUtils.toString(v1, false) +
                          NodeUtils.toString(v2, false));

    } else if (v1.isArray() && v2.isArray())
      // if both are arrays: array concatenation
      return concatenateArrays(v1, v2);

    else if (v1.isObject() && v2.isObject())
      // if both are objects: object union
      return unionObjects(v1, v2);

    // {} + null => {} (also arrays)
    else if ((v1.isObject() || v1.isArray()) && v2.isNull())
      return v1;

    // null + {} => {} (also arrays)
    else if (v1.isNull() && (v2.isObject() || v2.isArray()))
      return v2;

    else
      // do numeric operation
      return super.perform(v1, v2);
  }

  protected double perform(double v1, double v2) {
    return v1 + v2;
  }

  protected long perform(long v1, long v2) {
    return v1 + v2;
  }

  private JsonArray concatenateArrays(JsonValue v1, JsonValue v2) {
    // .addAll is faster than many .add() calls
    JsonArray result = new JsonArray();
    result.addAll((JsonArray) v1);
    result.addAll((JsonArray) v2);
    return result;
  }

  private JsonObject unionObjects(JsonValue v1, JsonValue v2) {
    // .putAll is faster than many .set() calls
    JsonObject result = new JsonObject();
    result.putAll((JsonObject) v2);
    result.putAll((JsonObject) v1); // v1 should overwrite v2
    return result;
  }
}
