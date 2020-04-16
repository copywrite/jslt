
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

import com.schibsted.spt.data.jslt.json.JsonDouble;
import com.schibsted.spt.data.jslt.json.JsonLong;
import com.schibsted.spt.data.jslt.json.JsonNull;
import com.schibsted.spt.data.jslt.json.JsonValue;

public abstract class NumericOperator extends AbstractOperator {

  public NumericOperator(ExpressionNode left, ExpressionNode right, String name,
                         Location location) {
    super(left, right, name, location);
  }

  public JsonValue perform(JsonValue v1, JsonValue v2) {
    if (v1.isNull() || v2.isNull())
      return JsonNull.instance;

    v1 = NodeUtils.number(v1, true, location);
    v2 = NodeUtils.number(v2, true, location);

    if (v1.isIntegralNumber() && v2.isIntegralNumber())
      return new JsonLong(perform(v1.longValue(), v2.longValue()));
    else
      return new JsonDouble(perform(v1.doubleValue(), v2.doubleValue()));
  }

  protected abstract double perform(double v1, double v2);

  protected abstract long perform(long v1, long v2);
}
