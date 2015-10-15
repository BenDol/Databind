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
package nz.co.doltech.databind.reflect.jre;

import nz.co.doltech.databind.reflect.FieldReflection;

public class FieldReflectionJre implements FieldReflection {
    private java.lang.reflect.Field field;

    public FieldReflectionJre(java.lang.reflect.Field field) {
        this.field = field;
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

    @Override
    public void setValue(Object object, Object value) {
        try {
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getValue(Object object) {
        try {
            @SuppressWarnings("unchecked")
            T result = (T) field.get(object);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void copyValueTo(Object source, Object destination) {
        try {
            field.set(destination, field.get(source));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getModifier() {
        return field.getModifiers();
    }

    @Override
    public String toString() {
        return "[FieldJre " + field.getType().getName() + " " + field.getName() + "]";
    }
}
