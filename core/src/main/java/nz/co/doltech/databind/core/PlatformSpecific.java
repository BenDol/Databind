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
import nz.co.doltech.databind.core.properties.DynamicPropertyBag;

public interface PlatformSpecific {
    DynamicPropertyBag getObjectDynamicPropertyBag(Object object);

    void setObjectDynamicPropertyBag(Object object, DynamicPropertyBag bag);

    boolean isBindingToken(String token);

    <T> T getBindingValue(Object object, String token);

    boolean setBindingValue(Object object, String name, Object value);

    PropertyAdapter createPropertyAdapter(Object object);

    // Metadata

    void setObjectMetadata(Object object, Object metadata);

    <T> T getObjectMetadata(Object object);

    // Model Mapper

    boolean isSpecificDataAdapter(Object object);

    void fillSpecificDataAdapter(Object widget, Object context, String property, Class<?> srcPptyType, DataAdapterInfo res);
}