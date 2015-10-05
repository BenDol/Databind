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
 * A PropertyAdapter implementation that is only able to set its target property value
 * <p/>
 * This adapter can only be used with a data binding target and CANNOT be used as a data binding source
 *
 * @author Arnaud
 */
public abstract class WriteOnlyPropertyAdapter implements PropertyAdapter {
    @Override
    public final Object registerPropertyChanged(Action1<PropertyAdapter, Object> callback, Object cookie) {
        return null;
    }

    @Override
    public final void removePropertyChangedHandler(Object handler) {
        if (handler != null) {
            throw new RuntimeException("Not implemented 'removePropertyChangedHandler' method in WriteOnlyDataAdapter");
        }
    }

    @Override
    public final Object getValue() {
        throw new RuntimeException("Not implemented 'getValue' method in WriteOnlyDataAdapter");
    }
}
