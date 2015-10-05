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
package nz.co.doltech.databind.core;

import nz.co.doltech.databind.core.properties.PropertyChangedHandler;

/**
 * Interface for objects which let others subscribe to property change events
 */
public interface NotifyPropertyChanged {
    /**
     * Registers a handler on <i>propertyName</i> preperty of the object.<i>handler</i> will be called each
     * time the property value changes.
     *
     * @param propertyName The name of the object's property to watch value for
     * @param handler      Instance of the handler that will receive notifications
     * @return An opaque object that can be used for unregistration
     */
    Object registerPropertyChangedEvent(String propertyName, PropertyChangedHandler handler);

    /**
     * Unregister a property change handler
     *
     * @param handlerRegistration The handler to unregister from the property system
     */
    void removePropertyChangedHandler(Object handlerRegistration);
}
