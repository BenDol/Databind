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
package nz.co.doltech.databind.classinfo.internal;

import java.util.ArrayList;
import java.util.List;

import nz.co.doltech.databind.classinfo.ClassInfo;
import nz.co.doltech.databind.classinfo.Clazz;
import nz.co.doltech.databind.classinfo.Field;
import nz.co.doltech.databind.classinfo.Method;

public abstract class ClazzBase<T> implements Clazz<T> {
    private Class<? super T> _superClass;
    private Class<T> _reflectedClass;
    private String _className;
    private List<Field> _allFields;
    private List<Field> _declaredFields;
    private List<Field> _fields;
    private List<Method> _methods;

    @SuppressWarnings("unused")
    private ClazzBase() {
    }

    @SuppressWarnings("unchecked")
    protected ClazzBase(Class<?> reflectedClass, String className, Class<? super T> superClass) {
        _reflectedClass = (Class<T>) reflectedClass;
        _className = className;
        _superClass = superClass;
    }

    // To implement :
    protected abstract List<Field> _getDeclaredFields();

    protected abstract List<Method> _getMethods();

    protected abstract void _ensureSuperClassInfoRegistered();

    @Override
    public Clazz<? super T> getSuperclass() {
        if (_superClass == null)
            return null;

        _ensureSuperClassInfoRegistered();

        return ClassInfo.clazz(_superClass);
    }

    @Override
    public String getClassName() {
        return _className;
    }

    @Override
    public Class<T> getReflectedClass() {
        _ensureSuperClassInfoRegistered();

        return _reflectedClass;
    }

    @Override
    public List<Field> getAllFields() {
        if (_allFields == null) {
            _allFields = _getDeclaredFields();

            // all public declared fields of superclass
            Clazz<? super T> superClass = getSuperclass();
            if (superClass != null)
                _allFields.addAll(superClass.getAllFields());
        }

        return _allFields;
    }

    @Override
    public Field getAllField(String name) {
        // first, search in declared fields
        for (Field field : getDeclaredFields())
            if (field.getName().equals(name))
                return field;

        // then try superclass
        Clazz<? super T> superClass = getSuperclass();
        if (superClass != null)
            return superClass.getAllField(name);

        return null;
    }

    @Override
    public List<Field> getDeclaredFields() {
        if (_declaredFields == null) {
            _declaredFields = _getDeclaredFields();
        }

        return _declaredFields;
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
        if (_fields == null) {
            // all public declared fields
            _fields = new ArrayList<Field>();
            for (Field field : getDeclaredFields()) {
                if ((field.getModifier() & /*Modifier.PUBLIC*/1) == /*Modifier.PUBLIC*/1) {
                    _fields.add(field);
                }
            }

            // all public declared fields of superclass
            Clazz<? super T> superClass = getSuperclass();
            if (superClass != null) {
                _fields.addAll(superClass.getDeclaredFields());
            }
        }

        return _fields;
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

    @Override
    public List<Method> getMethods() {
        if (_methods == null) {
            _methods = new ArrayList<>();
            _methods.addAll(_getMethods());
        }

        return _methods;
    }

    @Override
    public Method getMethod(String methodName) {
        for (Method method : getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}