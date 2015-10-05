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
package nz.co.doltech.databind.core.propertyadapters;

import nz.co.doltech.databind.core.properties.PropertyChangedEvent;
import nz.co.doltech.databind.core.properties.PropertyChangedHandler;
import nz.co.doltech.databind.util.Action1;
import nz.co.doltech.databind.core.properties.Properties;

/**
 * A PropertyAdapter implementation which is able to work with an object's field or property
 * <p/>
 * To access the object's property, first the adapter tries to find a getter/setter. Then, if no
 * access method is found, the adapter works with the object's field value directly.
 *
 * @author Arnaud
 */
public class ObjectPropertyAdapter implements PropertyAdapter, PropertyChangedHandler {
    private final Object source;
    private final String sourceProperty;

    private Action1<PropertyAdapter, Object> callback;
    private Object cookie;

    public ObjectPropertyAdapter(Object source, String sourceProperty) {
        this.source = source;
        this.sourceProperty = sourceProperty;
    }

    @Override
    public Object registerPropertyChanged(Action1<PropertyAdapter, Object> callback, Object cookie) {
        if (source == null) {
            return null;
        }

        this.callback = callback;
        this.cookie = cookie;

        return Properties.register(source, sourceProperty, this);
    }

    @Override
    public void removePropertyChangedHandler(Object registration) {
        Properties.removeHandler(registration);
    }

    @Override
    public Object getValue() {
        return Properties.getValue(source, sourceProperty);
    }

    @Override
    public void setValue(Object value) {
        Properties.setValue(source, sourceProperty, value);
    }

    @Override
    public void onPropertyChanged(PropertyChangedEvent event) {
        if (callback == null) {
            return;
        }

        callback.exec(this, cookie);
    }
}