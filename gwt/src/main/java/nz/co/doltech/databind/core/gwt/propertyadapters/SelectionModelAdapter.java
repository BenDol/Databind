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

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import nz.co.doltech.databind.core.propertyadapters.PropertyAdapter;
import nz.co.doltech.databind.util.Action1;

/**
 * A PropertAdapter to bind a {@link SingleSelectionModel}.<br/>
 * <p/>
 * - When receiving an object, the adapter will set it as the selected object.<br/>
 * - When the selected item changes, the binding system gets triggered.
 *
 * @author Arnaud
 */
public class SelectionModelAdapter<T> implements PropertyAdapter {
    private SingleSelectionModel<T> model;

    /**
     * Uses the given {@link SingleSelectionModel} and acts as a binding property.
     * Used to bind the model's selected value to another value.
     */
    public SelectionModelAdapter(SingleSelectionModel<T> selectionModel) {
        this.model = selectionModel;
    }

    @Override
    public void removePropertyChangedHandler(Object handlerRegistration) {
        ((HandlerRegistration) handlerRegistration).removeHandler();
    }

    @Override
    public Object registerPropertyChanged(final Action1<PropertyAdapter, Object> callback, final Object cookie) {
        return model.addSelectionChangeHandler(new Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                callback.exec(SelectionModelAdapter.this, cookie);
            }
        });
    }

    @Override
    public Object getValue() {
        return model.getSelectedObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(Object object) {
        model.clear();
        model.setSelected((T) object, true);
    }
}