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
package nz.co.doltech.databind.classinfo.gwt;

import java.util.HashMap;
import java.util.Set;

import nz.co.doltech.databind.classinfo.Clazz;
import nz.co.doltech.databind.classinfo.IClassInfo;
import nz.co.doltech.databind.classinfo.internal.ObjectClazz;

public class ClassInfoGwt implements IClassInfo {
    private HashMap<Class<?>, Clazz<?>> clazzMap;

    /**
     * Obtain a runtime type information on a class.<br/>
     * <br/>
     * <p/>
     * Throws a RuntimeException if the type information provider is not found.
     *
     * @param clazz The class object for which type information is required
     * @return The runtime information interface
     */
    public <T> Clazz<T> clazz(Class<T> clazz) {
        _ensureMap();

        @SuppressWarnings("unchecked")
        Clazz<T> res = (Clazz<T>) this.clazzMap.get(clazz);
        if (res == null)
            throw new IllegalArgumentException("Class not supported by ClassInfo : " + clazz.getName());

        return res;
    }

    /**
     * Register a runtime type information provider
     */
    public <T> void registerClazz(Clazz<T> clazz) {
        _ensureMap();

        if (this.clazzMap.containsKey(clazz.getReflectedClass()))
            return;

        this.clazzMap.put(clazz.getReflectedClass(), clazz);
    }

    /**
     * Obtain a runtime type information on a class.
     *
     * @param name Name of the class for which type information is required
     * @return The runtime information interface
     */
    public Clazz<?> findClazz(String name) {
        if (clazzMap == null)
            return null;

        for (Clazz<?> c : clazzMap.values())
            if (c.getClassName().equals(name))
                return c;

        return null;
    }

    /**
     * Obtain a runtime type information on a class.
     *
     * @param clazz The class object for which type information is required
     * @return The runtime information interface
     */
    public <T> Clazz<T> findClazz(Class<T> clazz) {
        if (this.clazzMap == null)
            return null;

        for (Clazz<?> c : this.clazzMap.values())
            if (c.getReflectedClass() == clazz) {
                @SuppressWarnings("unchecked")
                Clazz<T> result = (Clazz<T>) c;
                return result;
            }

        return null;
    }

    /**
     * Retrieve the set of registered type information providers.
     */
    public Set<Class<?>> getRegisteredClazz() {
        if (clazzMap == null)
            return null;

        return clazzMap.keySet();
    }

    private void _ensureMap() {
        if (clazzMap != null)
            return;

        clazzMap = new HashMap<>();
        clazzMap.put(java.lang.Object.class, new ObjectClazz());
    }
}
