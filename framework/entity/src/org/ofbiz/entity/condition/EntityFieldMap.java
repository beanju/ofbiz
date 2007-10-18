/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.ofbiz.entity.condition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ofbiz.entity.util.EntityUtil;

/**
 * Encapsulates simple expressions used for specifying queries
 *
 */
public class EntityFieldMap extends EntityConditionListBase<EntityExpr> {

    protected Map<String, ? extends Object> fieldMap;

    protected EntityFieldMap() {
        super();
    }

    public static List<EntityExpr> makeConditionList(EntityComparisonOperator op, Object... keysValues) {
        return makeConditionList(EntityUtil.makeFields(keysValues), op);
    }

    public static List<EntityExpr> makeConditionList(Map<String, ? extends Object> fieldMap, EntityComparisonOperator op) {
        if (fieldMap == null) return new ArrayList<EntityExpr>();
        List<EntityExpr> list = new ArrayList<EntityExpr>(fieldMap.size());
        for (Map.Entry<String, ? extends Object> entry: fieldMap.entrySet()) {
            list.add(new EntityExpr(entry.getKey(), op, entry.getValue()));
        }
        return list;
    }

    public EntityFieldMap(EntityComparisonOperator compOp, EntityJoinOperator joinOp, Object... keysValues) {
        this(EntityUtil.makeFields(keysValues), compOp, joinOp);
    }

    public EntityFieldMap(Map<String, ? extends Object> fieldMap, EntityComparisonOperator compOp, EntityJoinOperator joinOp) {
        super(makeConditionList(fieldMap, compOp), joinOp);
        this.fieldMap = fieldMap;
        if (this.fieldMap == null) this.fieldMap = new LinkedHashMap<String, Object>();
        this.operator = joinOp;
    }

    public EntityFieldMap(EntityJoinOperator operator, Object... keysValues) {
        this(EntityOperator.EQUALS, operator, keysValues);
    }

    public EntityFieldMap(Map<String, ? extends Object> fieldMap, EntityJoinOperator operator) {
        this(fieldMap, EntityOperator.EQUALS, operator);
    }

    public Object getField(String name) {
        return this.fieldMap.get(name);
    }
    
    public boolean containsField(String name) {
        return this.fieldMap.containsKey(name);
    }
    
    public Iterator<String> getFieldKeyIterator() {
        return Collections.unmodifiableSet(this.fieldMap.keySet()).iterator();
    }
    
    public Iterator<Map.Entry<String, Object>> getFieldEntryIterator() {
        return Collections.unmodifiableMap(this.fieldMap).entrySet().iterator();
    }
    
    public void accept(EntityConditionVisitor visitor) {
        visitor.acceptEntityFieldMap(this);
    }
}
