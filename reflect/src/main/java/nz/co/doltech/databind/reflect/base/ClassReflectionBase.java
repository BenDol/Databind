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

import nz.co.doltech.databind.reflect.ClassReflection;
import nz.co.doltech.databind.reflect.Reflections;
import nz.co.doltech.databind.reflect.FieldReflection;

public abstract class ClassReflectionBase<T> implements ClassReflection<T> {
    private String className;
    private Class<? super T> superClass;
    private Class<T> reflectedClass;

    private List<FieldReflection> allFields;
    private List<FieldReflection> declaredFields;
    private List<FieldReflection> fields;

    private ClassReflectionBase() {
    }

    @SuppressWarnings("unchecked")
    protected ClassReflectionBase(Class<?> reflectedClass, String className, Class<? super T> superClass) {
        this.reflectedClass = (Class<T>) reflectedClass;
        this.className = className;
        this.superClass = superClass;
    }

    /**
     * Load the registered declared fields.
     */
    protected abstract List<FieldReflection> loadDeclaredFields();

    /**
     * Ensure the super class is registered.
     */
    protected abstract void ensureSuperClassRegistered();

    @Override
    public ClassReflection<? super T> getSuperclass() {
        if (superClass == null) {
            return null;
        }

        ensureSuperClassRegistered();

        return Reflections.reflect(superClass);
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
    public List<FieldReflection> getAllFields() {
        if (allFields == null) {
            allFields = loadDeclaredFields();

            // all public declared fields of superclass
            ClassReflection<? super T> superClass = getSuperclass();
            if (superClass != null) {
                allFields.addAll(superClass.getAllFields());
            }
        }

        return allFields;
    }

    @Override
    public FieldReflection getAllField(String name) {
        // first, search in declared fields
        for (FieldReflection field : getDeclaredFields()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }

        // then try superclass
        ClassReflection<? super T> superClass = getSuperclass();
        if (superClass != null) {
            return superClass.getAllField(name);
        }

        return null;
    }

    @Override
    public List<FieldReflection> getDeclaredFields() {
        if (declaredFields == null) {
            declaredFields = loadDeclaredFields();
        }

        return declaredFields;
    }

    @Override
    public FieldReflection getDeclaredField(String name) {
        for (FieldReflection field : getDeclaredFields()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public List<FieldReflection> getFields() {
        if (fields == null) {
            // all public declared fields
            fields = new ArrayList<FieldReflection>();
            for (FieldReflection field : getDeclaredFields()) {
                if ((field.getModifier() & /*Modifier.PUBLIC*/1) == /*Modifier.PUBLIC*/1) {
                    fields.add(field);
                }
            }

            // all public declared fields of superclass
            ClassReflection<? super T> superClass = getSuperclass();
            if (superClass != null) {
                fields.addAll(superClass.getDeclaredFields());
            }
        }
        return fields;
    }

    @Override
    public FieldReflection getField(String fieldName) {
        for (FieldReflection field : getFields()) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }
}