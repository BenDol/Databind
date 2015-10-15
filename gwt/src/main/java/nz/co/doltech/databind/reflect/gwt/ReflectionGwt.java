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
package nz.co.doltech.databind.reflect.gwt;

import nz.co.doltech.databind.reflect.ClassReflection;
import nz.co.doltech.databind.reflect.Reflection;
import nz.co.doltech.databind.reflect.base.ObjectClassReflection;

import java.util.HashMap;
import java.util.Set;

public class ReflectionGwt implements Reflection {
    private HashMap<Class<?>, ClassReflection<?>> clazzMap;

    public ReflectionGwt() {
        clazzMap = new HashMap<>();
        clazzMap.put(java.lang.Object.class, new ObjectClassReflection());
    }

    /**
     * Obtain a runtime type information on a class.<br/>
     * Throws a RuntimeException if the type information provider is not found.
     *
     * @param clazz The class object for which type information is required
     * @return The runtime information interface
     */
    @SuppressWarnings("unchecked")
    public <T> ClassReflection<T> reflect(Class<T> clazz) {
        ClassReflection<T> res = (ClassReflection<T>) this.clazzMap.get(clazz);
        if (res == null) {
            throw new IllegalArgumentException("Class not supported by Reflection : " + clazz.getName());
        }

        return res;
    }

    /**
     * Register a runtime type information provider
     */
    public <T> void registerClass(ClassReflection<T> clazz) {
        if (this.clazzMap.containsKey(clazz.getReflectedClass())) {
            return;
        }
        this.clazzMap.put(clazz.getReflectedClass(), clazz);
    }

    /**
     * Obtain a runtime type information on a class.
     *
     * @param name Name of the class for which type information is required
     * @return The runtime information interface
     */
    public ClassReflection<?> findClass(String name) {
        for (ClassReflection<?> c : clazzMap.values()) {
            if (c.getClassName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Obtain a runtime type information on a class.
     *
     * @param clazz The class object for which type information is required
     * @return The runtime information interface
     */
    @SuppressWarnings("unchecked")
    public <T> ClassReflection<T> findClass(Class<T> clazz) {
        for (ClassReflection<?> c : this.clazzMap.values()) {
            if (c.getReflectedClass() == clazz) {
                return (ClassReflection<T>) c;
            }
        }
        return null;
    }

    /**
     * Retrieve the set of registered type information providers.
     */
    public Set<Class<?>> getAllRegistered() {
        return clazzMap.keySet();
    }
}
