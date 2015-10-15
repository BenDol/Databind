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

import nz.co.doltech.databind.reflect.Clazz;

/**
 * Utility class supporting the concept of Property.
 * <p/>
 * A Property on an object is a value that can be get and/or set through either
 * a getter/setter or directly through the object's field.
 *
 * @author Arnaud Tournier
 * @author Ben Dol
 */
public class Properties {
    private final static PropertyAccessImpl propertyAccess = new PropertyAccessImpl();
    private final static PropertyChanges propertyChanges = new PropertyChanges();

    /**
     * Returns the class of the property
     */
    public static Class<?> getPropertyClassType(Clazz<?> clazz, String name) {
        return propertyAccess.getFieldClassType(clazz, name);
    }

    /**
     * Gets the property's value from an object
     *
     * @param object The object
     * @param name   Property name
     */
    public static <T> T getValue(Object object, String name) {
        return propertyAccess.getValue(object, name);
    }

    /**
     * Sets a value on an object's property
     *
     * @param object       the object on which the property is set
     * @param propertyName the name of the property value to be set
     * @param value        the new value of the property
     */
    public static boolean setValue(Object object, String propertyName, Object value) {
        return propertyAccess.setValue(object, propertyName, value);
    }

    /**
     * Registers an handler for a specific property change on an object. The object
     * itself does not need to implement anything. But at least it should call the
     * {@link #notify(Object, String)} method when its internal property changes.
     *
     * @param source       The object from which one wants notifications
     * @param propertyName The property subscribed. You can use "*" to subscribe to all properties in one time
     */
    public static Object register(Object source, String propertyName, PropertyChangedHandler handler) {
        return propertyChanges.register(source, propertyName, handler);
    }

    /**
     * Unregisters a handler, freeing associated resources
     *
     * @param handlerRegistration The object received after a call to {@link PropertyChanges}
     */
    public static void removeHandler(Object handlerRegistration) {
        propertyChanges.removeHandler(handlerRegistration);
    }

    /**
     * Notifies the event system of an object changing one
     * of its properties.
     *
     * @param sender       The object whom property changed
     * @param propertyName The changed property name
     */
    public static void notify(Object sender, String propertyName) {
        propertyChanges.notify(sender, propertyName);
    }

    /**
     * Obtain useful information for debugging. That's useful
     * to detect registration leaks.
     */
    public static String getStatistics() {
        return propertyChanges.getStatistics();
    }

    /**
     * Whether a getter or a field is available with that name.
     */
    public static boolean canAccessField(Clazz<?> clazz, String name) {
        return propertyAccess.hasFieldAccess(clazz, name);
    }

    /**
     * Returns the class of the setter property. It can be the class of the first
     * argument in the setter or the class of the field if no setter is found.
     * If a virtual property is used, it returns null or the class of the current
     * property's value.
     */
    public static Class<?> getFieldClassType(Clazz<?> clazz, String name) {
        return propertyAccess.getFieldClassType(clazz, name);
    }

    /**
     * Gets a dynamic property value on an object
     *
     * @param object       the object from which one wants to get the property value
     * @param propertyName the property name
     */
    public static <T> T getObjectDynamicProperty(Object object, String propertyName) {
        return propertyAccess.getObjectDynamicProperty(object, propertyName);
    }

    /**
     * Whether a dynamic property value has already been set on this object
     */
    public static boolean hasObjectDynamicProperty(Object object, String propertyName) {
        return propertyAccess.hasObjectDynamicProperty(object, propertyName);
    }

    /**
     * Sets a dynamic property value on an object.
     */
    public static void setObjectDynamicProperty(Object object, String propertyName, Object value) {
        propertyAccess.setObjectDynamicProperty(object, propertyName, value);
    }
}
