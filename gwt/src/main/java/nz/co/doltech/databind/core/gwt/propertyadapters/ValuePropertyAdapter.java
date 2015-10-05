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
package nz.co.doltech.databind.core.gwt.propertyadapters;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

import nz.co.doltech.databind.core.propertyadapters.PropertyAdapter;
import nz.co.doltech.databind.util.Action1;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A PropertyAdapter implementation that works with a target
 * implementing the HasValue interface.
 * <p/>
 * Typically, this will be a Widget, like a TextBox for example,
 * that will receive and notify its changes
 *
 * @author Arnaud Tournier
 */
public class ValuePropertyAdapter implements PropertyAdapter, ValueChangeHandler<Object> {
    private static final Logger logger = Logger.getLogger(ValuePropertyAdapter.class.getName());

    HasValue<Object> hasValue;

    Action1<PropertyAdapter, Object> callback;
    Object cookie;

    @SuppressWarnings("unchecked")
    public ValuePropertyAdapter(HasValue<?> hasValue) {
        this.hasValue = (HasValue<Object>) hasValue;
    }

    @Override
    public Object registerPropertyChanged(Action1<PropertyAdapter, Object> callback, Object cookie) {
        this.callback = callback;
        this.cookie = cookie;

        return hasValue.addValueChangeHandler(this);
    }

    @Override
    public void onValueChange(ValueChangeEvent<Object> event) {
        if (callback != null)
            callback.exec(this, cookie);
    }

    @Override
    public Object getValue() {
        return hasValue.getValue();
    }

    @Override
    public void setValue(Object object) {
        try {
            hasValue.setValue(object, true);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to setValue for object '"
                + hasValue.getClass().getName() + "' using the value: " + object);

            throw ex;
        }
    }

    @Override
    public void removePropertyChangedHandler(Object handler) {
        ((HandlerRegistration) handler).removeHandler();
    }
}