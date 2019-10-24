
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

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.List;

public class ValExpression extends AbstractNode {
    private String variable;
    private ExpressionNode value;
    private ExpressionNode body;
    private int slot; // this variable's position in the stack frame
    private VariableInfo info;

    public ValExpression(String variable, ExpressionNode value, ExpressionNode body, Location location) {
        super(location);
        this.variable = variable;
        this.value = value;
        this.body = body;
        this.slot = ScopeManager.UNFOUND;
    }

    public String getVariable() {
        return variable;
    }

    public int getSlot() {
        return slot;
    }

    public JsonNode apply(Scope scope, JsonNode input) {
        JsonNode val = value.apply(scope, input);
        scope.setValue(getSlot(), val);
        return body.apply(scope, input);
    }

    public void computeMatchContexts(DotExpression parent) {
        value.computeMatchContexts(parent);
        body.computeMatchContexts(parent);
    }

    public void dump(int level) {
        System.out.println(NodeUtils.indent(level) +
                "val " + variable + " =");
        value.dump(level + 1);
        System.out.println(NodeUtils.indent(level) + "into");
        body.dump(level + 1);
    }

    public List<ExpressionNode> getChildren() {
        return Arrays.asList(value, body);
    }

    public ExpressionNode optimize() {
        value = value.optimize();
        body = body.optimize();
        return this;
    }

    public void register(ScopeManager scope) {
        info = scope.registerVariable(new VariableInfo(getLocation()) {
            @Override
            public String getName() {
                return getVariable();
            }

            @Override
            public ExpressionNode getDeclaration() {
                return ValExpression.this.getDeclaration();
            }
        });
        slot = info.getSlot();
    }

    public ExpressionNode getDeclaration() {
        return value;
    }


    public void prepare(PreparationContext ctx) {
        value.prepare(ctx);
        ctx.scope.enterScope();
        register(ctx.scope);
        body.prepare(ctx);
        ctx.scope.leaveScope();
    }
}
