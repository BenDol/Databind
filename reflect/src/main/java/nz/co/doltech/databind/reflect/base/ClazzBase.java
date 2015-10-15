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

import java.util.ArrayList;
import java.util.List;

import nz.co.doltech.databind.reflect.Reflection;
import nz.co.doltech.databind.reflect.Clazz;
import nz.co.doltech.databind.reflect.Field;

public abstract class ClazzBase<T> implements Clazz<T> {
    private Class<? super T> superClass;
    private Class<T> reflectedClass;
    private String className;
    private List<Field> allFields;
    private List<Field> declaredFields;
    private List<Field> fields;

    @SuppressWarnings("unused")
    private ClazzBase() {
    }

    @SuppressWarnings("unchecked")
    protected ClazzBase(Class<?> reflectedClass, String className, Class<? super T> superClass) {
        this.reflectedClass = (Class<T>) reflectedClass;
        this.className = className;
        this.superClass = superClass;
    }

    protected abstract List<Field> _getDeclaredFields();

    protected abstract void ensureSuperClassRegistered();

    @Override
    public Clazz<? super T> getSuperclass() {
        if (superClass == null)
            return null;

        ensureSuperClassRegistered();

        return Reflection.clazz(superClass);
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public Class<T> getReflectedClass() {
        ensureSuperClassRegistered();

        return reflectedClass;
    }

    @Override
    public List<Field> getAllFields() {
        if (allFields == null) {
            allFields = _getDeclaredFields();

            // all public declared fields of superclass
            Clazz<? super T> superClass = getSuperclass();
            if (superClass != null) {
                allFields.addAll(superClass.getAllFields());
            }
        }

        return allFields;
    }

    @Override
    public Field getAllField(String name) {
        // first, search in declared fields
        for (Field field : getDeclaredFields())
            if (field.getName().equals(name))
                return field;

        // then try superclass
        Clazz<? super T> superClass = getSuperclass();
        if (superClass != null) {
            return superClass.getAllField(name);
        }

        return null;
    }

    @Override
    public List<Field> getDeclaredFields() {
        if (declaredFields == null) {
            declaredFields = _getDeclaredFields();
        }

        return declaredFields;
    }

    @Override
    public Field getDeclaredField(String name) {
        for (Field field : getDeclaredFields()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public List<Field> getFields() {
        if (fields == null) {
            // all public declared fields
            fields = new ArrayList<Field>();
            for (Field field : getDeclaredFields()) {
                if ((field.getModifier() & /*Modifier.PUBLIC*/1) == /*Modifier.PUBLIC*/1) {
                    fields.add(field);
                }
            }

            // all public declared fields of superclass
            Clazz<? super T> superClass = getSuperclass();
            if (superClass != null) {
                fields.addAll(superClass.getDeclaredFields());
            }
        }

        return fields;
    }

    @Override
    public Field getField(String fieldName) {
        for (Field field : getFields()) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }
}