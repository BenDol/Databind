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

import nz.co.doltech.databind.classinfo.Method;

import java.util.ArrayList;
import java.util.List;

public class MethodJre implements Method {
    private java.lang.reflect.Method method;
    private List<Class<?>> parameterTypes;

    public MethodJre(java.lang.reflect.Method method) {
        this.method = method;
        if (!method.isAccessible())
            method.setAccessible(true);
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    @Override
    public List<Class<?>> getParameterTypes() {
        if (parameterTypes != null)
            return parameterTypes;

        Class<?>[] arr = method.getParameterTypes();

        parameterTypes = new ArrayList<>();

        for (Class<?> c : arr) {
            parameterTypes.add(c);
        }

        return parameterTypes;
    }

    @Override
    public Object invoke(Object target, Object... parameters) {
        try {
            return method.invoke(target, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "[MethodJre " + method.getName() + "]";
    }
}