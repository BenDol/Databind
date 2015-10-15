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

import nz.co.doltech.databind.reflect.Clazz;
import nz.co.doltech.databind.reflect.ClassInfo;

import java.util.HashMap;
import java.util.Set;

public class ClassInfoJre implements ClassInfo {
    private static final ClassInfoJre instance = new ClassInfoJre();
    HashMap<Class<?>, ClazzJre<?>> clazzCache = new HashMap<>();

    private ClassInfoJre() {
    }

    public static ClassInfoJre get() {
        return instance;
    }

    @Override
    public <T> Clazz<T> clazz(Class<T> clazz) {
        ClazzJre<T> result = findClazz(clazz);
        if (result == null) {
            throw new RuntimeException("Cannot find class '" + clazz.getName() + "'");
        }

        return result;
    }

    @Override
    public <T> void registerClazz(Clazz<T> clazz) {
        clazzCache.put(clazz.getReflectedClass(), (ClazzJre<?>) clazz);
    }

    @Override
    public Clazz<?> findClazz(String name) {
        try {
            return findClazz(Class.forName(name));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public <T> ClazzJre<T> findClazz(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        ClazzJre<T> result = (ClazzJre<T>) clazzCache.get(clazz);
        if (result == null) {
            result = new ClazzJre<T>(clazz);
            clazzCache.put(clazz, result);
        }

        return result;
    }

    @Override
    public Set<Class<?>> getAllRegistered() {
        return clazzCache.keySet();
    }
}