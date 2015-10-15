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

import java.util.HashMap;

import nz.co.doltech.databind.core.propertyadapters.PropertyAdapter;
import nz.co.doltech.databind.core.properties.DynamicPropertyBag;

class PlatformSpecificJre implements PlatformSpecific {
    private static final PlatformSpecificJre INSTANCE = new PlatformSpecificJre();
    private static HashMap<Integer, DynamicPropertyBag> propertyBags = new HashMap<>();
    private static HashMap<Integer, Object> metadatas = new HashMap<>();

    private PlatformSpecificJre() {
    }

    public static PlatformSpecificJre get() {
        return INSTANCE;
    }

    public void setObjectDynamicPropertyBag(Object object, DynamicPropertyBag bag) {
        propertyBags.put(System.identityHashCode(object), bag);
    }

    public DynamicPropertyBag getObjectDynamicPropertyBag(Object object) {
        return propertyBags.get(System.identityHashCode(object));
    }

    @Override
    public boolean isBindingToken(String token) {
        return false;
    }

    @Override
    public <T> T getBindingValue(Object object, String token) {
        return null;
    }

    @Override
    public boolean setBindingValue(Object object, String name, Object value) {
        return false;
    }

    // Metadata

    @Override
    public PropertyAdapter createPropertyAdapter(Object object) {
        return null;
    }

    @Override
    public void setObjectMetadata(Object object, Object metadata) {
        metadatas.put(System.identityHashCode(object), metadata);
    }

    @Override
    public <T> T getObjectMetadata(Object object) {
        @SuppressWarnings("unchecked")
        T result = (T) metadatas.get(System.identityHashCode(object));
        return result;
    }

    // Model Mapper

    @Override
    public boolean isSpecificDataAdapter(Object object) {
        return false;
    }

    @Override
    public void processDataAdapter(Object widget, Object context, String property, Class<?> srcPptyType, DataAdapterInfo res) {
        throw new IllegalStateException();
    }
}