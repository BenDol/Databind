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
package nz.co.doltech.databind.core.gwt;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

import nz.co.doltech.databind.core.Converter;
import nz.co.doltech.databind.core.Mode;
import nz.co.doltech.databind.core.gwt.propertyadapters.ListBoxPropertyAdapter;
import nz.co.doltech.databind.core.gwt.propertyadapters.ValuePropertyAdapter;
import nz.co.doltech.databind.core.propertyadapters.PropertyAdapter;

/**
 * Second part of the fluent API for Data Binding. When a binding is
 * prepared by calling a method on the Binder class, an instance of
 * this class is returned to finish the binding creation process.
 *
 * @author Arnaud Tournier
 * @author Ben Dol
 */
public class BindingCreation extends nz.co.doltech.databind.core.BindingCreation {
    protected boolean deferActivate;

    public BindingCreation(PropertyAdapter source) {
        super(source);
    }

    /**
     * Final step, defines the data binding destination and activates the
     * binding.
     * <p/>
     * The object used as the binding's destination is a HasValue widget, like a
     * TextBox. The binding system will the use setValue, getValue and
     * addValueChangeHandler methods to set, get and get change notifications on
     * the @param widget parameter.
     *
     * @param hasValue The {@link HasValue} object
     * @return The DataBinding object
     */
    public DataBinding to(HasValue<?> hasValue) {
        return to(new ValuePropertyAdapter(hasValue));
    }

    /**
     * Final step, defines the data binding destination and activates the
     * binding.
     * <p/>
     * The object used as the binding's destination is a ListBox widget.
     * The binding system will the use setValue, getValue and addChangeHandler
     * methods to set, get and get change notifications on the @param widget parameter.
     *
     * @param listBox The widget
     * @return The DataBinding object
     */
    public DataBinding to(ListBox listBox) {
        return to(new ListBoxPropertyAdapter(listBox));
    }

    @Override
    public BindingCreation log(String prefix) {
        super.log(prefix);
        return this;
    }

    /**
     * Second step, parameters.
     * <p/>
     * The created data binding will be activated at the next event loop. The
     * Scheduler.get().scheduleDeferred() method will be used.
     *
     * @return The Binder to continue specifying the data binding
     */
    public BindingCreation deferActivate() {
        deferActivate = true;

        return this;
    }

    @Override
    public BindingCreation mode(Mode mode) {
        super.mode(mode);
        return this;
    }

    @Override
    public BindingCreation withConverter(Converter converter) {
        super.withConverter(converter);
        return this;
    }

    @Override
    public DataBinding to(PropertyAdapter destination) {
        // create the binding according to the parameters
        DataBinding binding = new DataBinding(source, destination, mode, converter, logPrefix);

        // activate the binding : launch a value event
        if (deferActivate) {
            binding.deferActivate();
        } else {
            binding.activate();
        }

        return binding;
    }
}
