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
import nz.co.doltech.databind.reflect.Reflection;

import java.util.HashMap;
import java.util.Set;

public class ReflectionJre implements Reflection {
    private static final ReflectionJre instance = new ReflectionJre();
    HashMap<Class<?>, ClassReflectionJre<?>> clazzCache = new HashMap<>();

    private ReflectionJre() {
    }

    public static ReflectionJre get() {
        return instance;
    }

    @Override
    public <T> ClassReflection<T> reflect(Class<T> clazz) {
        ClassReflectionJre<T> result = findClass(clazz);
        if (result == null) {
            throw new RuntimeException("Cannot find class '" + clazz.getName() + "'");
        }

        return result;
    }

    @Override
    public <T> void registerClass(ClassReflection<T> clazz) {
        clazzCache.put(clazz.getReflectedClass(), (ClassReflectionJre<?>) clazz);
    }

    @Override
    public ClassReflection<?> findClass(String name) {
        try {
            return findClass(Class.forName(name));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public <T> ClassReflectionJre<T> findClass(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        ClassReflectionJre<T> result = (ClassReflectionJre<T>) clazzCache.get(clazz);
        if (result == null) {
            result = new ClassReflectionJre<T>(clazz);
            clazzCache.put(clazz, result);
        }

        return result;
    }

    @Override
    public Set<Class<?>> getAllRegistered() {
        return clazzCache.keySet();
    }
}