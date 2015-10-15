/*
 * Copyright 2015 Doltech Systems Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package nz.co.doltech.databind.reflect.base;

import nz.co.doltech.databind.reflect.FieldReflection;

public abstract class FieldReflectionBase implements FieldReflection {
    private final Class<?> clazz;
    private final String fieldName;
    private int modifier;

    protected FieldReflectionBase(Class<?> clazz, String fieldName, int modifier) {
        this.clazz = clazz;
        this.fieldName = fieldName;
        this.modifier = modifier;
    }

    @Override
    public String getName() {
        return fieldName;
    }

    @Override
    public Class<?> getType() {
        return clazz;
    }

    @Override
    public int getModifier() {
        return modifier;
    }
}