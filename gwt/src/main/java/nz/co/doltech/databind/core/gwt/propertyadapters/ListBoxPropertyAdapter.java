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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ListBox;
import nz.co.doltech.databind.core.Converter;
import nz.co.doltech.databind.core.propertyadapters.PropertyAdapter;
import nz.co.doltech.databind.util.Action1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * A PropertyAdapter to bind a {@link ListBox}.<br/>
 * <p/>
 * - When receiving an object, the adapter will set it as the selected object.<br/>
 * - When the selected item changes, the binding system gets triggered.<br/>
 * - When using a multiselect ListBox you need to ensure you are binding a collection.
 * <br/><br/>
 * When the {@link ListBox} has multiselect enabled, {@link #getValue()} returns an
 * {@link ArrayList} of String representing the selected values. <br/>
 * If you need a different collection type to be mapped create a {@link Converter}.
 *
 * @author Ben Dol
 */
public class ListBoxPropertyAdapter implements PropertyAdapter {
    private Logger logger = Logger.getLogger(ListBoxPropertyAdapter.class.getName());

    private ListBox listBox;

    /**
     * Uses the given {@link ListBox} and acts as a binding property.
     * Used to bind the objects change value to another value.
     */
    public ListBoxPropertyAdapter(ListBox listBox) {
        this.listBox = listBox;
    }

    @Override
    public Object getValue() {
        if (listBox.isMultipleSelect()) {
            List<String> selectedItems = new ArrayList<>();
            for (int i = 0; i < listBox.getItemCount(); i++) {
                if (listBox.isItemSelected(i)) {
                    selectedItems.add(listBox.getValue(i));
                }
            }
            return selectedItems;
        } else {
            return listBox.getSelectedValue();
        }
    }

    @Override
    public void setValue(Object object) {
        deselectItems();
        if (object instanceof Collection) {
            for (Object item : (Collection<?>) object) {
                selectItem(item);
            }
        } else {
            selectItem(object);
        }
    }

    @Override
    public void removePropertyChangedHandler(Object handlerRegistration) {
        ((HandlerRegistration) handlerRegistration).removeHandler();
    }

    @Override
    public Object registerPropertyChanged(final Action1<PropertyAdapter, Object> callback, final Object cookie) {
        return listBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                callback.exec(ListBoxPropertyAdapter.this, cookie);
            }
        });
    }

    private void selectItem(Object item) {
        int index = getValueIndex(item);
        if (index > -1) {
            listBox.setItemSelected(index, true);
        } else {
            logger.warning("Failed to select item using: " + item.toString()
                + " " + item.getClass().getName());
        }
    }

    private void deselectItems() {
        for (int i = 0; i < listBox.getItemCount(); i++) {
            if (listBox.isItemSelected(i)) {
                listBox.setItemSelected(i, false);
            }
        }
    }

    private int getValueIndex(Object value) {
        for (int i = 0; i < listBox.getItemCount(); i++) {
            if (listBox.getValue(i).equals(String.valueOf(value))) {
                return i;
            }
        }
        return -1;
    }
}