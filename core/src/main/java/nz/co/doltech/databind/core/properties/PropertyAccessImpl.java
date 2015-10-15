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
package nz.co.doltech.databind.core.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import nz.co.doltech.databind.reflect.ClassReflection;
import nz.co.doltech.databind.reflect.Reflections;
import nz.co.doltech.databind.reflect.FieldReflection;
import nz.co.doltech.databind.core.PlatformSpecific;
import nz.co.doltech.databind.core.PlatformSpecificProvider;
import nz.co.doltech.databind.core.propertyadapters.CompositePropertyAdapter;
import nz.co.doltech.databind.core.tools.Property;

class PropertyAccessImpl implements PropertyAccessor {
    private final static Logger logger = Logger.getLogger(PropertyAccessImpl.class.getName());

    private static class PropertyTypeCache {
        Map<String, Class<?>> propertyTypes = new HashMap<>();

        public Class<?> getPropertyType(String name) {
            return propertyTypes.get(name);
        }

        public void setPropertyType(String name, Class<?> type) {
            propertyTypes.put(name, type);
        }
    }

    private final static PlatformSpecific propertyBagAccess = PlatformSpecificProvider.get();
    private final static Map<Integer, PropertyTypeCache> propertyTypeCache = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(Object object, String name) {
        T result = getPropertyImpl(object, name);

        if (result instanceof Property) {
            return ((Property<T>) result).getValue();
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setValue(Object object, String propertyName, Object value) {
        if(object == null) {
            return false;
        }
        ClassReflection<?> s = Reflections.reflect(object.getClass());

        if (Property.class == getFieldClassType(s, propertyName)) {
            Property<Object> property = getPropertyImpl(object, propertyName);
            if (property != null) {
                property.setValue(value);
                return true;
            }
            return false;
        }

        return setPropertyImpl(s, object, propertyName, value);
    }

    @Override
    public boolean hasFieldAccess(ClassReflection<?> clazz, String name) {
        return getFieldClassType(clazz, name) != null;
    }

    @Override
    public Class<?> getFieldClassType(ClassReflection<?> clazz, String name) {
        PropertyTypeCache cache = getCache(clazz);

        Class<?> res = cache.getPropertyType(name);
        if (res != null) {
            return res;
        } else {
            FieldReflection field = clazz.getAllField(name);
            if (field != null) {
                res = field.getType();

                if (res != null) {
                    cache.setPropertyType(name, res);
                }
            }
        }
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObjectDynamicProperty(Object object, String propertyName) {
        DynamicPropertyBag bag = propertyBagAccess.getObjectDynamicPropertyBag(object);
        return bag != null ? (T) bag.get(propertyName) : null;
    }

    @Override
    public void setObjectDynamicProperty(Object object, String propertyName, Object value) {
        DynamicPropertyBag bag = propertyBagAccess.getObjectDynamicPropertyBag(object);
        if (bag == null) {
            bag = new DynamicPropertyBag();
            propertyBagAccess.setObjectDynamicPropertyBag(object, bag);
        }
        bag.set(propertyName, value);

        Properties.notify(object, propertyName);
    }

    @Override
    public boolean hasObjectDynamicProperty(Object object, String propertyName) {
        DynamicPropertyBag bag = propertyBagAccess.getObjectDynamicPropertyBag(object);
        return bag != null && bag.contains(propertyName);
    }

    private <T> T getPropertyImpl(Object object, String name) {
        if (PlatformSpecificProvider.get().isBindingToken(name)) {
            return PlatformSpecificProvider.get().getBindingValue(object, name);
        }

        if (name.equals(CompositePropertyAdapter.MODELMAP_TOKEN)) {
            throw new RuntimeException(
                "Property of type $ModelMap cannot be readded.");
        }

        // if has dynamic-property, return it !
        if (hasObjectDynamicProperty(object, name)) {
            logger.fine("'" + name + "' read dynamic property on object " + object);
            return getObjectDynamicProperty(object, name);
        }

        ClassReflection<?> s = Reflections.reflect(object.getClass());

        // try direct field access
        FieldReflection field = s.getAllField(name);
        if (field != null) {
            return field.getValue(object);
        }

        // Maybe a dynamic property will be set later on
        logger.warning("DataBinding: Warning: assuming that the object would " +
            "in the future have a dynamic property set / Maybe have an opt-in " +
            "option on the Binding to clarify things");

        return null;
    }

    private boolean setPropertyImpl(ClassReflection<?> s, Object object, String name, Object value) {
        if (PlatformSpecificProvider.get().isBindingToken(name)) {
            return PlatformSpecificProvider.get().setBindingValue(
                object, name, value);
        }

        FieldReflection field = s.getAllField(name);
        if (field != null) {
            field.setValue(object, value);
            Properties.notify(object, name);
            return true;
        }

        if (!hasObjectDynamicProperty(object, name)) {
            logger.warning("'"
                + name + "' write dynamic property on object "
                + object.getClass().getName() + " with value " + value + " "
                + "WARNING : That means there is no field for that class, "
                + "please ensure this is intentional.");
        }

        setObjectDynamicProperty(object, name, value);

        return false;
    }

    private PropertyTypeCache getCache(ClassReflection<?> clazz) {
        Integer key = System.identityHashCode(clazz);

        PropertyTypeCache res = propertyTypeCache.get(key);
        if (res == null) {
            res = new PropertyTypeCache();
            propertyTypeCache.put(key, res);
        }
        return res;
    }
}
