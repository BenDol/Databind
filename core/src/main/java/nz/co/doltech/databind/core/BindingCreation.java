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

import nz.co.doltech.databind.core.propertyadapters.CompositePropertyAdapter;
import nz.co.doltech.databind.core.propertyadapters.PropertyAdapter;

/**
 * Second part of the fluent API for Data Binding. When a binding is
 * prepared by calling a method on the Binder class, an instance of
 * this class is returned to finish the binding creation process.
 *
 * @author Arnaud Tournier
 * @author Ben Dol
 */
public class BindingCreation {
    protected final PropertyAdapter source;
    protected Mode mode = Mode.TwoWay;
    protected Converter converter;
    protected String logPrefix;

    public BindingCreation(PropertyAdapter source) {
        this.source = source;
    }

    /**
     * Second step, parameters.
     * <p/>
     * Defines the data binding mode
     *
     * @return The Binder to continue specifying the data binding
     */
    public BindingCreation mode(Mode mode) {
        this.mode = mode;

        return this;
    }

    /**
     * Second step, parameters.
     * <p/>
     * Defines the logging prefix used to log data binding state.
     *
     * @return The Binder to continue specifying the data binding
     */
    public BindingCreation log(String prefix) {
        this.logPrefix = prefix;

        return this;
    }

    /**
     * Second step, parameters.
     * <p/>
     * Defines a converter to be used by the data binding system when getting
     * and setting values.
     *
     * @return The Binder to continue specifying the data binding
     */
    public BindingCreation withConverter(Converter converter) {
        this.converter = converter;

        return this;
    }

    /**
     * Final step, defines the data binding destination and activates the
     * binding
     * <p/>
     * The destination value is searched as specified in the @param path, in the
     * context of the @param destination object.
     * <p/>
     * For example : <i>...To( customer, "company.address.city" )</i> can be
     * used to access data at different depths. If all intermediary steps
     * provide a correct implementation for the Data Binding mechanism, any
     * change at any depth will be catched.
     *
     * @param destination  The destination object
     * @param propertyPath The destination object's property path
     * @return The DataBinding object
     */
    public DataBinding to(Object destination, String propertyPath) {
        return to(new CompositePropertyAdapter(destination, propertyPath));
    }

    /**
     * Final step, defines the data binding destination and activates the
     * binding
     * <p/>
     * The object used as the binding's destination will be mapped to the source
     * object. Each of the matching properties of the source and destination
     * will be two-way bound, so that a change in one gets written in the other
     * one.
     *
     * @param destination The object that will be mapped to the source
     * @return The DataBinding object
     */
    public DataBinding mapTo(Object destination) {
        return mode(Mode.OneWay).to(new CompositePropertyAdapter(destination, CompositePropertyAdapter.MODELMAP_TOKEN));
    }

    /**
     * Final step, defines the data binding destination and activates the
     * binding
     * <p/>
     * This method accepts any implementation of PropertyAdapter, especially
     * user ones so that is a good start to customize the data binding
     * possibilities
     *
     * @param destination The destination property adapter
     * @return The DataBinding object
     */
    public DataBinding to(PropertyAdapter destination) {
        // create the binding according to the parameters
        DataBinding binding = new DataBinding(source, destination, mode, converter, logPrefix);

        // activate the binding
        binding.activate();

        return binding;
    }
}
