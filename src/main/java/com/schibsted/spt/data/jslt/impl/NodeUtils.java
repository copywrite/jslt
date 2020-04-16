
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

import java.io.IOException;
import java.util.Map;
import java.util.Iterator;

import com.schibsted.spt.data.jslt.JsltException;
import com.schibsted.spt.data.jslt.json.*;

public class NodeUtils {
  public static void evalLets(Scope scope, JsonValue input, LetExpression[] lets) {
    if (lets == null)
      return;

    for (int ix = 0; ix < lets.length; ix++) {
      String var = lets[ix].getVariable();
      JsonValue val = lets[ix].apply(scope, input);
      scope.setValue(lets[ix].getSlot(), val);
    }
  }

  public static boolean isTrue(JsonValue value) {
    return value != JsonBoolean.FALSE &&
      !(value.isObject() && value.size() == 0) &&
      !(value.isString() && value.stringValue().length() == 0) &&
      !(value.isArray() && value.size() == 0) &&
      !(value.isNumber() && value.doubleValue() == 0.0) &&
      !value.isNull();
  }

  public static boolean isValue(JsonValue value) {
    return !value.isNull() &&
      !(value.isObject() && value.size() == 0) &&
      !(value.isArray() && value.size() == 0);
  }

  public static JsonValue toJson(boolean value) {
    if (value)
      return JsonBoolean.TRUE;
    else
      return JsonBoolean.FALSE;
  }

  public static JsonValue toJson(double value) {
    return new JsonDouble(value);
  }

  public static JsonValue toJson(String[] array) {
    JsonArray node = new JsonArray();
    for (int ix = 0; ix < array.length; ix++)
      node.add(array[ix]);
    return node;
  }

  // nullok => return Java null for Json null
  public static String toString(JsonValue value, boolean nullok) {
    // check what type this is
    if (value.isString())
      return value.stringValue();
    else if (value.isNull() && nullok)
      return null;

    // not sure how well this works in practice, but let's try
    return value.toString();
  }

  public static JsonArray toArray(JsonValue value, boolean nullok) {
    // check what type this is
    if (value.isArray())
      return (JsonArray) value;
    else if (value.isNull() && nullok)
      return null;

    throw new JsltException("Cannot convert " + value + " to array");
  }

  public static JsonValue number(JsonValue value, Location loc) {
    return number(value, false, loc);
  }

  public static JsonValue number(JsonValue value, boolean strict, Location loc) {
    // this works, because Java null can never be a function parameter
    // in JSTL, unlike JSON null
    return number(value, strict, loc, null);
  }

  public static JsonValue number(JsonValue value, boolean strict, Location loc,
                                 JsonValue fallback) {
    // check what type this is
    if (value.isNumber())
      return value;
    else if (value.isNull()) {
      if (fallback == null)
        return value;
      else
        return fallback;
    } else if (!value.isString()) {
      if (strict)
        throw new JsltException("Can't convert " + value + " to number", loc);
      else if (fallback == null)
        return JsonNull.instance;
      else
        return fallback;
    }

    // let's look at this number. There are a ton of number formats,
    // so just let Jackson handle it.
    String number = value.asText();
    JsonValue numberNode = null;
    try {
        numberNode = mapper.readTree(number);
    } catch (IOException e) {}

    if (numberNode == null || !numberNode.isNumber()) {
      if (fallback == null)
        throw new JsltException("number(" + number + ") failed: not a number",
                                loc);
      else
        return fallback;
    } else {
        return numberNode;
    }
  }

  public static JsonArray convertObjectToArray(JsonValue object) {
    JsonArray array = new JsonArray();
    Iterator<Map.Entry<String, JsonValue>> it = object.fields();
    while (it.hasNext()) {
      Map.Entry<String, JsonValue> item = it.next();
      JsonObject element = new JsonObject();
      element.put("key", new JsonString(item.getKey()));
      element.put("value", item.getValue());
      array.add(element);
    }
    return array;
  }

  public static String indent(int level) {
    char[] indent = new char[level * 2];
    for (int ix = 0; ix < indent.length; ix++)
      indent[ix] = ' ';
    return new String(indent, 0, indent.length);
  }
}
