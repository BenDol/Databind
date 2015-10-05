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

import nz.co.doltech.databind.util.Action1;

/**
 * Allows the data binding system to work with any object's property
 * <p/>
 * A property will generally be a field in a class, or a pair of getter/setter.
 * It can also be an adapter to HasValue<T> widgets, and so on...
 *
 * @author Arnaud Tournier
 */
public interface PropertyAdapter {
    /**
     * Gets the Property value
     *
     * @return The Property value
     */
    Object getValue();

    /**
     * Sets the Property value
     *
     * @param object The value to be set
     */
    void setValue(Object object);

    /**
     * Registers a Property change event handler
     *
     * @param callback Instance of a callback which will be called on Property value change, with the PropertyAdpater instance and cookie value
     * @param cookie   A cookie, this object will be given back in notifications
     */
    Object registerPropertyChanged(Action1<PropertyAdapter, Object> callback, Object cookie);

    /**
     * Unregisters a handler
     *
     * @param handlerRegistration The handler to be unregistered
     */
    void removePropertyChangedHandler(Object handlerRegistration);
}