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
import nz.co.doltech.databind.core.properties.Properties;

/**
 * A PropertyAdapter that calls its onChange method when a property value changes
 * in the currently bound object.
 *
 * @author Arnaud
 */
public abstract class ChangeDetector extends WriteOnlyPropertyAdapter {
    PropertyChangedHandler handler = new PropertyChangedHandler() {
        @Override
        public void onPropertyChanged(PropertyChangedEvent event) {
            onChange(event.getSender(), event.getPropertyName());
        }
    };
    private Object reg;

    abstract protected void onChange(Object object, String property);

    @Override
    public void setValue(Object object) {
        if (reg != null) {
            Properties.removeHandler(reg);
            reg = null;
        }

        if (object != null) {
            reg = Properties.register(object, "*", handler);
        }
    }
}