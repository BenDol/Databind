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
package nz.co.doltech.databind.core.gwt;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;

import nz.co.doltech.databind.core.DefaultConverters;
import nz.co.doltech.databind.core.DataAdapterInfo;
import nz.co.doltech.databind.core.PlatformSpecific;
import nz.co.doltech.databind.core.gwt.propertyadapters.ValuePropertyAdapter;
import nz.co.doltech.databind.core.properties.DynamicPropertyBag;
import nz.co.doltech.databind.core.propertyadapters.CompositePropertyAdapter;
import nz.co.doltech.databind.core.propertyadapters.PropertyAdapter;
import nz.co.doltech.databind.core.tools.Property;

public final class PlatformSpecificGwt implements PlatformSpecific {
    private static final PlatformSpecificGwt INSTANCE;

    static {
        Logger.getLogger(PlatformSpecificGwt.class.getName()).info("PlatformSpecificGwt Initiated");

        INSTANCE = new PlatformSpecificGwt();
    }

    private PlatformSpecificGwt() {
    }

    public static PlatformSpecificGwt get() {
        return INSTANCE;
    }

    @Override
    public DynamicPropertyBag getObjectDynamicPropertyBag(Object object) {
        if (GWT.isScript()) {
            return getObjectDynamicPropertyBagImpl(object);
        } else {
            return DynamicPropertyBagAccessJre.getObjectDynamicPropertyBag(object);
        }
    }

    private native DynamicPropertyBag getObjectDynamicPropertyBagImpl(Object object) /*-{
        return object.__databind_dynamic_ppty_bag || null;
    }-*/;

    @Override
    public void setObjectDynamicPropertyBag(Object object, DynamicPropertyBag bag) {
        if (GWT.isScript()) {
            setObjectDynamicPropertyBagImpl(object, bag);
        } else {
            DynamicPropertyBagAccessJre.setObjectDynamicPropertyBag(object, bag);
        }
    }

    private native void setObjectDynamicPropertyBagImpl(Object object, DynamicPropertyBag bag) /*-{
        object.__databind_dynamic_ppty_bag = bag;
    }-*/;

    @Override
    public boolean isBindingToken(String token) {
        return token.equals(CompositePropertyAdapter.HASVALUE_TOKEN);
    }

    @Override
    public <T> T getBindingValue(Object object, String token) {
        @SuppressWarnings({"rawtypes", "unchecked"})
        T result = (T) ((HasValue) object).getValue();
        return result;
    }

    @Override
    public boolean setBindingValue(Object object, String name, Object value) {
        assert object instanceof HasValue : "Object should be implementing HasValue<?> !";

        @SuppressWarnings("unchecked")
        HasValue<Object> hasValue = ((HasValue<Object>) object);

        hasValue.setValue(value, true);
        return true;
    }

    @Override
    public PropertyAdapter createPropertyAdapter(Object object) {
        return new ValuePropertyAdapter((HasValue<?>) object);
    }

    @Override
    public void setObjectMetadata(Object object, Object metadata) {
        if (GWT.isScript()) {
            setObjectMetadataImpl(object, metadata);
        } else {
            MetatdataJre.setObjectMetadata(object, metadata);
        }
    }

    // Metadata

    private native void setObjectMetadataImpl(Object object, Object metadata) /*-{
        object.__databind_metadata = metadata;
    }-*/;

    @Override
    public <T> T getObjectMetadata(Object object) {
        if (GWT.isScript()) {
            return getObjectMetadataImpl(object);
        } else {
            return MetatdataJre.getObjectMetadata(object);
        }
    }

    private native <T> T getObjectMetadataImpl(Object object) /*-{
        return object.__databind_metadata || null;
    }-*/;

    @Override
    public boolean isSpecificDataAdapter(Object object) {
        return object instanceof HasValue;
    }

    @Override
    public void processDataAdapter(Object object, Object context, String property, Class<?> srcType,
                                   DataAdapterInfo res) {
        // try to guess the HasValue type
        res.setDataType(Object.class);
        if (object instanceof HasText) {
            res.setDataType(String.class);
        }

        String debugString = "";

        // try to find a converter if dataType does not match srcType
        Class<?> dataType = res.getDataType();
        if (srcType != null && dataType != null && dataType != srcType && srcType != Property.class) {
            // try to find a converter, if not : fail
            res.setConverter(DefaultConverters.findConverter(srcType, dataType));
            if (res.getConverter() == null) {
                debugString = "[ERROR: Cannot find converter from " + srcType + " to " + dataType + "]";
            } else {
                debugString = "[" + srcType.getSimpleName() + ">" + dataType.getSimpleName() + "] " + debugString;
            }
        }

        debugString += "\"" + property + ".$HasValue\"";
        res.setDebugString(debugString);

        res.setAdapter(new CompositePropertyAdapter(context, property + ".$HasValue"));
    }

    // Model Mapper

    private static class DynamicPropertyBagAccessJre {
        private static HashMap<Integer, DynamicPropertyBag> propertyBags = new HashMap<>();

        static void setObjectDynamicPropertyBag(Object object, DynamicPropertyBag bag) {
            propertyBags.put(System.identityHashCode(object), bag);
        }

        static DynamicPropertyBag getObjectDynamicPropertyBag(Object object) {
            return propertyBags.get(System.identityHashCode(object));
        }
    }

    private static class MetatdataJre {
        private static final HashMap<Integer, Object> metadataMap = new HashMap<>();

        static void setObjectMetadata(Object object, Object metadata) {
            metadataMap.put(System.identityHashCode(object), metadata);
        }

        static <T> T getObjectMetadata(Object object) {
            @SuppressWarnings("unchecked")
            T result = (T) metadataMap.get(System.identityHashCode(object));
            return result;
        }
    }
}