package nz.co.doltech.databind.core.properties;

import nz.co.doltech.databind.reflect.Clazz;

public interface PropertyAccessor {
    /**
     * Gets the property's value from an object.
     */
    <T> T getValue(Object object, String name);

    /**
     * Sets a value on an object's property
     *
     * @param object       the object on which the property is set
     * @param propertyName the name of the property value to be set
     * @param value        the new value of the property
     */
    boolean setValue(Object object, String propertyName, Object value);

    /**
     * Has access to a given field name.
     * @return null if has no access.
     */
    boolean hasFieldAccess(Clazz<?> clazz, String name);

    /**
     * Get a fields {@link Class} type.
     */
    Class<?> getFieldClassType(Clazz<?> clazz, String name);

    /**
     * Gets a dynamic property value on an object
     *
     * @param object       the object from which one wants to get the property value
     * @param propertyName the property name
     */
    <T> T getObjectDynamicProperty(Object object, String propertyName);

    /**
     * Sets a dynamic property value on an object.
     */
    void setObjectDynamicProperty(Object object, String propertyName, Object value);

    /**
     * Whether a dynamic property value has already been set on this object
     */
    boolean hasObjectDynamicProperty(Object object, String propertyName);
}
