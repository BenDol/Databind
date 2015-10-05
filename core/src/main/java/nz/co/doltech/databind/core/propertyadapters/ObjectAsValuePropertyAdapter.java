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
 * This property adapter takes an object and uses it as the property value.
 * It is thus not possible for this adapter to accept new values. It will
 * throw an exception in that case.
 *
 * @author Arnaud Tournier
 * @author Ben Dol
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
    public Object registerPropertyChanged(Action1<PropertyAdapter, Object> callback, Object cookie) {
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
