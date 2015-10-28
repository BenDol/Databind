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
package nz.co.doltech.databind.reflect;

import java.util.Set;

/**
 * Frontal singleton entry point providing the Reflection system API
 */
public class Reflections {
    private static Reflection impl = ReflectionProvider.get();

    /**
     * Obtain a runtime type information on a class.<br><br>
     * <p/>
     * Throws a RuntimeException if the type information provider is not found.
     *
     * @param clazz The class object for which type information is required
     * @return The runtime information interface
     */
    public static <T> ClassReflection<T> reflect(Class<T> clazz) {
        return impl.reflect(clazz);
    }

    /**
     * Register a runtime type information provider
     */
    public static <T> void registerClass(ClassReflection<T> clazz) {
        impl.registerClass(clazz);
    }

    /**
     * Obtain a runtime type information on a class.
     *
     * @param name Name of the class for which type information is required
     * @return The runtime information interface
     */
    public static ClassReflection<?> findClass(String name) {
        return impl.findClass(name);
    }

    /**
     * Obtain a runtime type information on a class.
     *
     * @param clazz The class object for which type information is required
     * @return The runtime information interface
     */
    public static <T> ClassReflection<T> findClass(Class<T> clazz) {
        return impl.findClass(clazz);
    }

    /**
     * Retrieve the set of registered type information providers.
     */
    public static Set<Class<?>> getAllRegistered() {
        return impl.getAllRegistered();
    }
}
