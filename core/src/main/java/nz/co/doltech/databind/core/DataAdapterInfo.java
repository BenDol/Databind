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
package nz.co.doltech.databind.core;

import nz.co.doltech.databind.core.propertyadapters.PropertyAdapter;

public final class DataAdapterInfo {
    PropertyAdapter adapter;
    Converter converter;
    Class<?> dataType;
    String debugString;

    public DataAdapterInfo() {
    }

    public DataAdapterInfo(PropertyAdapter adapter, Converter converter, Class<?> dataType, String debugString) {
        this.adapter = adapter;
        this.converter = converter;
        this.dataType = dataType;
        this.debugString = debugString;
    }

    public final PropertyAdapter getAdapter() {
        return adapter;
    }

    public final void setAdapter(PropertyAdapter adapter) {
        this.adapter = adapter;
    }

    public final Converter getConverter() {
        return converter;
    }

    public final void setConverter(Converter converter) {
        this.converter = converter;
    }

    public final Class<?> getDataType() {
        return dataType;
    }

    public final void setDataType(Class<?> dataType) {
        this.dataType = dataType;
    }

    public final String getDebugString() {
        return debugString;
    }

    public final void setDebugString(String debugString) {
        this.debugString = debugString;
    }
}