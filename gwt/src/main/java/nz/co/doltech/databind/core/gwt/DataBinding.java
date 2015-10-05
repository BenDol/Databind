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

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import nz.co.doltech.databind.core.Converter;
import nz.co.doltech.databind.core.Mode;
import nz.co.doltech.databind.core.propertyadapters.PropertyAdapter;

/**
 * DataBinding for GWT implementation.
 *
 * @author Ben Dol
 */
public class DataBinding extends nz.co.doltech.databind.core.DataBinding {

    public DataBinding(Object source, String sourceProperty, Object destination,
                       String destinationProperty, Mode bindingMode) {
        super(source, sourceProperty, destination, destinationProperty, bindingMode);
    }

    public DataBinding(PropertyAdapter source, PropertyAdapter destination, Mode bindingMode,
                       Converter converter, String logPrefix) {
        super(source, destination, bindingMode, converter, logPrefix);
    }

    @Override
    public DataBinding activate() {
        return (DataBinding) super.activate();
    }

    @Override
    public DataBinding suspend() {
        return (DataBinding) super.suspend();
    }

    /**
     * Activates the data binding using the deferred scheduler.
     *
     * @see #activate()
     */
    public void deferActivate() {
        log("deferred activation...");

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                activate();
            }
        });
    }
}
