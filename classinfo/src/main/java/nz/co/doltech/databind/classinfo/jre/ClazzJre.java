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
package nz.co.doltech.databind.classinfo.jre;

import nz.co.doltech.databind.classinfo.Clazz;
import nz.co.doltech.databind.classinfo.Field;
import nz.co.doltech.databind.classinfo.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClazzJre<T> implements Clazz<T> {
    private Class<T> classs;
    private List<Field> fields;
    private List<Method> methods;
    private Map<String, Method> methodsByName;

    public ClazzJre(Class<T> classs) {
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
    public Clazz<? super T> getSuperclass() {
        return ClassInfoJre.get().clazz(classs.getSuperclass());
    }

    @Override
    public List<Field> getAllFields() {
        if (fields != null)
            return fields;

        fields = new ArrayList<>();

        Class<?> cur = classs;
        while (cur != null && cur != Object.class) {
            for (java.lang.reflect.Field f : classs.getDeclaredFields())
                fields.add(new FieldJre(f));

            cur = cur.getSuperclass();
        }

        return fields;
    }

    @Override
    public Field getAllField(String fieldName) {
        for (Field field : getAllFields()) {
            if (field.getName().equals(fieldName))
                return field;
        }
        return null;
    }

    @Override
    public List<Field> getFields() {
        // TODO
        return getAllFields();
    }

    @Override
    public Field getField(String fieldName) {
        // TODO
        return getAllField(fieldName);
    }

    @Override
    public List<Field> getDeclaredFields() {
        // TODO
        return getAllFields();
    }

    @Override
    public Field getDeclaredField(String fieldName) {
        // TODO
        return getAllField(fieldName);
    }

    @Override
    public List<Method> getMethods() {
        if (methods != null)
            return methods;

        methods = new ArrayList<>();

        for (java.lang.reflect.Method m : classs.getMethods())
            methods.add(new MethodJre(m));

        return methods;
    }

    @Override
    public Method getMethod(String methodName) {
        if (methodsByName == null) {
            methodsByName = new HashMap<>();
            for (Method m : getMethods())
                methodsByName.put(m.getName(), m);
        }

        return methodsByName.get(methodName);
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

