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
package nz.co.doltech.databind.core.tools;

import nz.co.doltech.databind.core.properties.PropertyChangedEvent;
import nz.co.doltech.databind.core.properties.PropertyChangedHandler;
import nz.co.doltech.databind.core.propertyadapters.PropertyAdapter;
import nz.co.doltech.databind.util.Action1;
import nz.co.doltech.databind.core.properties.Properties;

/**
 * A class containing only one field : "value" that can be used as a source or
 * target for data binding.
 * <p/>
 * It can be used as a "holder" of a property
 *
 * @param <T> Type of the property value
 * @author Arnaud
 */
public class Property<T> implements PropertyAdapter {
    private Object owner;
    private String name;
    private T value;

    public Property(Object owner, String name, T value) {
        this.owner = owner;
        this.name = name;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        if (this.value == value) {
            return;
        }

        this.value = (T) value;

        Properties.notify(owner, name);
    }

    public Object register(PropertyChangedHandler handler) {
        return Properties.register(owner, name, handler);
    }

    public void removeRegistration(Object handlerRegistration) {
        Properties.removeHandler(handlerRegistration);
    }

    @Override
    public Object registerPropertyChanged(final Action1<PropertyAdapter, Object> callback, final Object cookie) {
        return register(new PropertyChangedHandler() {
            @Override
            public void onPropertyChanged(PropertyChangedEvent event) {
                callback.exec(Property.this, cookie);
            }
        });
    }

    @Override
    public void removePropertyChangedHandler(Object handlerRegistration) {
        removeRegistration(handlerRegistration);
    }
}
