
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

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.HashMap;

import com.schibsted.spt.data.jslt.Function;
import com.schibsted.spt.data.jslt.JsltException;
import com.schibsted.spt.data.jslt.json.*;

public class FunctionWrapper implements Function {
  private String name;
  private Method method;
  private ToJavaConverter[] converters;
  private ToJsonConverter returnConverter;

  public FunctionWrapper(String name, Method method) {
    this.name = name;
    this.method = method;
    this.returnConverter = makeJsonConverter(method.getReturnType());

    Class[] paramTypes = method.getParameterTypes();
    this.converters = new ToJavaConverter[paramTypes.length];
    for (int ix = 0; ix < paramTypes.length; ix++)
      converters[ix] = makeJavaConverter(paramTypes[ix]);
  }

  public String getName() {
    return name;
  }

  public int getMinArguments() {
    return method.getParameterCount();
  }

  public int getMaxArguments() {
    return method.getParameterCount();
  }

  public JsonValue call(JsonValue input, JsonValue[] arguments) {
    Object[] args = new Object[arguments.length];
    for (int ix = 0; ix < arguments.length; ix++)
      args[ix] = converters[ix].convert(arguments[ix]);

    try {
      Object result = method.invoke(null, args);
      return returnConverter.convert(result);
    } catch (IllegalAccessException e) {
      throw new JsltException("Couldn't call " + method, e);
    } catch (InvocationTargetException e) {
      throw new JsltException("Couldn't call " + method, e);
    }
  }

  // ===== TO JAVA

  interface ToJavaConverter {
    public Object convert(JsonValue node);
  }

  private static Map<Class, ToJavaConverter> toJava = new HashMap();
  static {
    toJava.put(String.class, new StringJavaConverter());
    toJava.put(int.class, new IntJavaConverter());
    toJava.put(long.class, new LongJavaConverter());
    toJava.put(boolean.class, new BooleanJavaConverter());
    toJava.put(double.class, new DoubleJavaConverter());
    toJava.put(float.class, new DoubleJavaConverter());
  }

  private static ToJavaConverter makeJavaConverter(Class type) {
    ToJavaConverter converter = toJava.get(type);
    if (converter == null)
      throw new JsltException("Cannot build converter to " + type);
    return converter;
  }

  static class StringJavaConverter implements ToJavaConverter {
    public Object convert(JsonValue node) {
      if (node.isNull())
        return null;
      else if (node.isString())
        return node.stringValue();
      else
        throw new JsltException("Could not convert " + node + " to string");
    }
  }

  static class LongJavaConverter implements ToJavaConverter {
    public Object convert(JsonValue node) {
      if (!node.isNumber())
        throw new JsltException("Cannot convert " + node + " to long");
      else
        return node.longValue();
    }
  }

  static class IntJavaConverter implements ToJavaConverter {
    public Object convert(JsonValue node) {
      if (!node.isNumber())
        throw new JsltException("Cannot convert " + node + " to int");
      else
        return node.intValue();
    }
  }

  static class BooleanJavaConverter implements ToJavaConverter {
    public Object convert(JsonValue node) {
      if (!node.isBoolean())
        throw new JsltException("Cannot convert " + node + " to boolean");
      else
        return node.booleanValue();
    }
  }

  static class DoubleJavaConverter implements ToJavaConverter {
    public Object convert(JsonValue node) {
      if (!node.isNumber())
        throw new JsltException("Cannot convert " + node + " to double");
      else
        return node.doubleValue();
    }
  }

  // ===== TO JSON

  interface ToJsonConverter {
    public JsonValue convert(Object node);
  }

  private static Map<Class, ToJsonConverter> toJson = new HashMap();
  static {
    toJson.put(String.class, new StringJsonConverter());
    toJson.put(long.class, new LongJsonConverter());
    toJson.put(int.class, new IntJsonConverter());
    toJson.put(boolean.class, new BooleanJsonConverter());
    toJson.put(double.class, new DoubleJsonConverter());
    toJson.put(float.class, new FloatJsonConverter());
  }

  static private ToJsonConverter makeJsonConverter(Class type) {
    ToJsonConverter converter = toJson.get(type);
    if (converter == null)
      throw new JsltException("Cannot build converter from " + type);
    return converter;
  }

  static class StringJsonConverter implements ToJsonConverter {
    public JsonValue convert(Object node) {
      if (node == null)
        return JsonNull.NULL;
      else
        return new JsonString((String) node);
    }
  }

  static class LongJsonConverter implements ToJsonConverter {
    public JsonValue convert(Object node) {
      if (node == null)
        return JsonNull.NULL;
      else
        return new JsonLong((Long) node);
    }
  }

  static class IntJsonConverter implements ToJsonConverter {
    public JsonValue convert(Object node) {
      if (node == null)
        return JsonNull.NULL;
      else
        return new JsonInt((Integer) node);
    }
  }

  static class BooleanJsonConverter implements ToJsonConverter {
    public JsonValue convert(Object node) {
      if (node == null)
        return JsonNull.NULL;
      else if ((Boolean) node)
        return JsonBoolean.TRUE;
      else
        return JsonBoolean.FALSE;
    }
  }

  static class DoubleJsonConverter implements ToJsonConverter {
    public JsonValue convert(Object node) {
      if (node == null)
        return JsonNull.NULL;
      else
        return new JsonDouble((Double) node);
    }
  }

  static class FloatJsonConverter implements ToJsonConverter {
    public JsonValue convert(Object node) {
      if (node == null)
        return JsonNull.NULL;
      else
        return new JsonDouble((Float) node);
    }
  }
}
