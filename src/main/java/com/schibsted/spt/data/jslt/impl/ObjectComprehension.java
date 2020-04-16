
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

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import com.schibsted.spt.data.jslt.JsltException;
import com.schibsted.spt.data.jslt.filters.JsonFilter;
import com.schibsted.spt.data.jslt.json.JsonObject;
import com.schibsted.spt.data.jslt.json.JsonValue;

public class ObjectComprehension extends AbstractNode {
  private ExpressionNode loop;
  private LetExpression[] lets;
  private ExpressionNode key;
  private ExpressionNode value;
  private ExpressionNode ifExpr;
  private JsonFilter filter;

  public ObjectComprehension(ExpressionNode loop,
                             LetExpression[] lets,
                             ExpressionNode key,
                             ExpressionNode value,
                             ExpressionNode ifExpr,
                             Location location,
                             JsonFilter filter) {
    super(location);
    this.loop = loop;
    this.lets = lets;
    this.key = key;
    this.value = value;
    this.ifExpr = ifExpr;
    this.filter = filter;
  }

  public JsonValue apply(Scope scope, JsonValue input) {
    JsonValue sequence = loop.apply(scope, input);
    if (sequence.isNull())
      return sequence;
    else if (sequence.isObject())
      sequence = NodeUtils.convertObjectToArray(sequence);
    else if (!sequence.isArray())
      throw new JsltException("Object comprehension can't loop over " + sequence, location);

    JsonObject object = new JsonObject();
    for (int ix = 0; ix < sequence.size(); ix++) {
      JsonValue context = sequence.get(ix);

      // must evaluate lets over again for each value because of context
      if (lets.length > 0)
        NodeUtils.evalLets(scope, context, lets);

      if (ifExpr == null || NodeUtils.isTrue(ifExpr.apply(scope, context))) {
        JsonValue valueNode = value.apply(scope, context);
        if (filter.filter(valueNode)) {
          // if there is no value, no need to evaluate the key
          JsonValue keyNode = key.apply(scope, context);
          if (!keyNode.isString())
            throw new JsltException("Object comprehension must have string as key, not " + keyNode, location);
          object.put(keyNode.stringValue(), valueNode);
        }
      }
    }
    return object;
  }

  public void prepare(PreparationContext ctx) {
    ctx.scope.enterScope();

    for (int ix = 0; ix < lets.length; ix++)
      lets[ix].register(ctx.scope);

    for (ExpressionNode child : getChildren())
      child.prepare(ctx);

    ctx.scope.leaveScope();
  }

  public List<ExpressionNode> getChildren() {
    List<ExpressionNode> children = new ArrayList();
    children.addAll(Arrays.asList(lets));
    children.add(loop);
    children.add(key);
    children.add(value);
    if (ifExpr != null)
      children.add(ifExpr);
    return children;
  }

  public ExpressionNode optimize() {
    for (int ix = 0; ix < lets.length; ix++)
      lets[ix].optimize();

    loop = loop.optimize();
    key = key.optimize();
    value = value.optimize();
    if (ifExpr != null)
      ifExpr = ifExpr.optimize();
    return this;
  }

  public void dump(int level) {
  }
}
