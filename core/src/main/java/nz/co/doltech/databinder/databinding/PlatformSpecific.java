package nz.co.doltech.databinder.databinding;

import nz.co.doltech.databinder.databinding.properties.DynamicPropertyBag;
import nz.co.doltech.databinder.databinding.propertyadapters.PropertyAdapter;

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

    // DTOMapper

    boolean isSpecificDataAdapter(Object object);

    void fillSpecificDataAdapter(Object widget, Object context, String property, Class<?> srcPptyType, DataAdapterInfo res);
}