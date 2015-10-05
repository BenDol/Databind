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

import nz.co.doltech.databind.classinfo.Field;
import nz.co.doltech.databind.classinfo.Method;

public class ObjectClazz extends ClazzBase<Object> {

    public ObjectClazz() {
        super(Object.class, "Object", null);
    }

    @Override
    protected List<Field> _getDeclaredFields() {
        return new ArrayList<>();
    }

    @Override
    protected List<Method> _getMethods() {
        return new ArrayList<>();
    }

    @Override
    public Object newInstance() {
        return new Object();
    }

    @Override
    protected void _ensureSuperClassInfoRegistered() {
    }
}
