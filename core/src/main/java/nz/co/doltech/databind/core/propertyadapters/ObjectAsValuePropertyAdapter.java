package nz.co.doltech.databind.core.propertyadapters;

import nz.co.doltech.databind.util.Action2;

/**
 * This property adapter takes an object and uses it as the property value.
 * It is thus not possible for this adapter to accept new values. It will
 * throw an exception in that case.
 *
 * @author Arnaud Tournier
 *         (c) LTE Consulting - 2015
 *         http://www.doltech.co.nz
 */
public class ObjectAsValuePropertyAdapter implements PropertyAdapter {
    private final Object source;

    public ObjectAsValuePropertyAdapter(Object source) {
        this.source = source;
    }

    @Override
    public void removePropertyChangedHandler(Object handlerRegistration) {
    }

    @Override
    public Object registerPropertyChanged(Action2<PropertyAdapter, Object> callback, Object cookie) {
        return null;
    }

    @Override
    public Object getValue() {
        return source;
    }

    @Override
    public void setValue(Object object) {
        throw new IllegalStateException("setValue impossible !");
    }
}
