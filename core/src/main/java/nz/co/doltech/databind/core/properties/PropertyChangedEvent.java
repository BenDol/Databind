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

/**
 * Holds necessary information to process a property changed event
 *
 * @author Arnaud Tournier
 */
public class PropertyChangedEvent {
    private final Object sender;

    private final String propertyName;

    /**
     * Constructor, can only be created by the
     * {@link #notify} method.
     */
    PropertyChangedEvent(Object sender, String propertyName) {
        this.sender = sender;
        this.propertyName = propertyName;
    }

    /**
     * Returns the object that sent the event
     *
     * @return The object that sent the event
     */
    public Object getSender() {
        return sender;
    }

    /**
     * Returns the name of the property that changed
     *
     * @return The name of the property that changed
     */
    public String getPropertyName() {
        return propertyName;
    }
}
