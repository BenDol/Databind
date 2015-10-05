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

import nz.co.doltech.databind.classinfo.Field;

public abstract class FieldBase implements Field {
    private final Class<?> _class;
    private final String _fieldName;
    private int _modifier;

    protected FieldBase(Class<?> clazz, String fieldName, int modifier) {
        _class = clazz;
        _fieldName = fieldName;
        _modifier = modifier;
    }

    @Override
    public String getName() {
        return _fieldName;
    }

    @Override
    public Class<?> getType() {
        return _class;
    }

    @Override
    public int getModifier() {
        return _modifier;
    }

    // @Override
    // public void copyValueTo(T source, T destination) {
    // if (_class == int.class)
    // setValue(destination, getValueInt(source));
    // else
    // setValue(destination, getValue(source));
    // }

    // To implement :
    // @Override
    // public native void setValue( T object, Object value )
    // /*-{
    // object@nz.co.doltech.databind.classinfo.internal.T::FIELD = value;
    // }-*/;
}