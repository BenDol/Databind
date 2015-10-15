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

import nz.co.doltech.databind.reflect.ClassReflection;
import nz.co.doltech.databind.reflect.FieldReflection;

import java.util.ArrayList;
import java.util.List;

public class ClassReflectionJre<T> implements ClassReflection<T> {
    private Class<T> classs;
    private List<FieldReflection> fields;

    public ClassReflectionJre(Class<T> classs) {
        this.classs = classs;
    }

    @Override
    public String getClassName() {
        return classs.getName();
    }

    @Override
    public Class<T> getReflectedClass() {
        return classs;
    }

    @Override
    public ClassReflection<? super T> getSuperclass() {
        return ReflectionJre.get().reflect(classs.getSuperclass());
    }

    @Override
    public List<FieldReflection> getAllFields() {
        if (fields != null) {
            return fields;
        }

        fields = new ArrayList<>();

        Class<?> cur = classs;
        while (cur != null && cur != Object.class) {
            for (java.lang.reflect.Field f : cur.getDeclaredFields()) {
                fields.add(new FieldReflectionJre(f));
            }

            cur = cur.getSuperclass();
        }

        return fields;
    }

    @Override
    public FieldReflection getAllField(String fieldName) {
        for (FieldReflection field : getAllFields()) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public List<FieldReflection> getFields() {
        // TODO
        return getAllFields();
    }

    @Override
    public FieldReflection getField(String fieldName) {
        // TODO
        return getAllField(fieldName);
    }

    @Override
    public List<FieldReflection> getDeclaredFields() {
        // TODO
        return getAllFields();
    }

    @Override
    public FieldReflection getDeclaredField(String fieldName) {
        // TODO
        return getAllField(fieldName);
    }

    @Override
    public T newInstance() {
        T result;
        try {
            result = classs.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}

