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

import nz.co.doltech.databind.classinfo.Method;

public abstract class MethodBase implements Method {
    private final Class<?> _returnValueClass;
    private final String _fieldName;
    private final Class<?>[] _parameterTypes;
    private List<Class<?>> _parameterTypesList;

    protected MethodBase(Class<?> clazz, String fieldName, Class<?>[] parameterTypes) {
        _returnValueClass = clazz;
        _fieldName = fieldName;
        _parameterTypes = parameterTypes;
    }

    @Override
    public String getName() {
        return _fieldName;
    }

    @Override
    public Class<?> getReturnType() {
        return _returnValueClass;
    }

    @Override
    public List<Class<?>> getParameterTypes() {
        if (_parameterTypesList == null) {
            _parameterTypesList = new ArrayList<>();
            for (Class<?> type : _parameterTypes) {
                _parameterTypesList.add(type);
            }
        }

        return _parameterTypesList;
    }
}
