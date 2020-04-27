
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
import java.math.BigInteger;
import java.util.Map;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
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
    // FIXME: This is probably not going to work ok.
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
        return JsonNull.NULL;
      else
        return fallback;
    }

    // let's look at this number.
    String number = JsonUtils.asText(value);
    JsonValue numberNode = parseNumber(number);
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

  // returns null in case of failure (caller then handles fallback)
  private static JsonValue parseNumber(String number) {
    if (number.length() == 0)
      return null;

    int pos = 0;
    if (number.charAt(0) == '-') {
      pos = 1;
    }

    int endInteger = scanDigits(number, pos);
    if (endInteger == pos)
      return null;
    if (endInteger == number.length()) {
      if (number.length() < 10)
        return new JsonInt(Integer.parseInt(number));
      else if (number.length() < 19)
        return new JsonLong(Long.parseLong(number));
      else
        throw new UnsupportedOperationException("Long overflow! No BigInt not implemented!");
        //return new BigIntegerNode(new BigInteger(number));
    }

    // since there's stuff after the initial integer it must be either
    // the decimal part or the exponent
    int intPart = Integer.parseInt(number.substring(0, endInteger));
    pos = endInteger;
    double value = intPart;

    if (number.charAt(pos) == '.') {
      pos += 1;
      int endDecimal = scanDigits(number, pos);
      if (endDecimal == pos)
        return null;

      long decimalPart = Long.parseLong(number.substring(endInteger + 1, endDecimal));
      int digits = endDecimal - endInteger - 1;

      value = (decimalPart / Math.pow(10, digits)) + intPart;
      pos = endDecimal;

      // if there's nothing more, then this is it
      if (pos == number.length())
        return new JsonDouble(value);
    }

    // there is more: next character MUST be 'e' or 'E'
    char ch = number.charAt(pos);
    if (ch != 'e' && ch != 'E')
      return null;

    // now we must have either '-', '+', or an integer
    pos++;
    if (pos == number.length())
      return null;
    ch = number.charAt(pos);
    int sign = 1;
    if (ch == '+')
      pos++;
    else if (ch == '-') {
      sign = -1;
      pos++;
    }

    int endExponent = scanDigits(number, pos);
    if (endExponent != number.length() || endExponent == pos)
      return null;

    int exponent = Integer.parseInt(number.substring(pos)) * sign;
    return new JsonDouble(value * Math.pow(10, exponent));
  }

  private static int scanDigits(String number, int pos) {
    while (pos < number.length() && isDigit(number.charAt(pos)))
      pos++;
    return pos;
  }

  private static boolean isDigit(char ch) {
    return ch >= '0' && ch <= '9';
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
