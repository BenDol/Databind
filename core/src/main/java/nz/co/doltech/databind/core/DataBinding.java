package nz.co.doltech.databind.core;

import java.util.logging.Logger;

import nz.co.doltech.databind.util.Action1;
import nz.co.doltech.databind.core.propertyadapters.ObjectPropertyAdapter;
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
import nz.co.doltech.databind.core.propertyadapters.PropertyAdapter;

/**
 * Manages the binding between a source and a destination.
 * <br/><br/>
 * The data binding has several options like OneWay, TwoWay, ...<br/>
 * The data propagation can happen synchronously after a data changed, or it can
 * happen asynchronously through a deferred command.
 *
 * @author Arnaud Tournier
 * @author Ben Dol
 */
public class DataBinding {
    private static final Logger logger = Logger.getLogger(DataBinding.class.getName());

    private final String logPrefix;

    private boolean activated;
    private boolean settingDestination;
    private boolean settingSource;

    private PropertyAdapter source;
    private Object sourceHandler;

    private PropertyAdapter destination;
    private Object destinationHandler;

    private Converter converter;

    private final Action1<PropertyAdapter, Object> onSourceChanged = new Action1<PropertyAdapter, Object>() {
        @Override
        public void exec(PropertyAdapter param, Object cookie) {
            // prevent us to wake up ourselves
            if (settingSource || !activated) {
                return;
            }

            if (logPrefix != null) {
                log("source changed, propagating to destination ...");
            }

            Object value = source.getValue();
            if (logPrefix != null) {
                log(" - source value : " + value);
            }

            if (converter != null) {
                if (logPrefix != null) {
                    log("... converting value ...");
                }

                value = converter.convert(value);
                if (logPrefix != null)
                    log(" - converted to : " + value);
            }

            settingDestination = true;
            destination.setValue(value);
            settingDestination = false;

            if (logPrefix != null) {
                log(" - done propagating source");
            }
        }
    };

    private final Action1<PropertyAdapter, Object> onDestinationChanged = new Action1<PropertyAdapter, Object>() {
        @Override
        public void exec(PropertyAdapter param, Object cookie) {
            // prevent us to wake up ourselves
            if (settingDestination || !activated)
                return;

            log("destination changed, propagating to source ...");

            Object value = destination.getValue();

            if (converter != null) {
                log("... converting value ...");
                value = converter.convertBack(value);
            }

            settingSource = true;
            source.setValue(value);
            settingSource = false;

            log("done setting destination to " + value);
        }
    };

    public DataBinding(Object source, String sourceProperty, Object destination, String destinationProperty, Mode bindingMode) {
        this(new ObjectPropertyAdapter(source, sourceProperty), new ObjectPropertyAdapter(destination, destinationProperty), bindingMode, null, null);
    }

    public DataBinding(PropertyAdapter source, PropertyAdapter destination, Mode bindingMode, Converter converter, String logPrefix) {
        this.source = source;
        this.destination = destination;
        this.converter = converter;
        this.logPrefix = logPrefix;

        switch (bindingMode) {
            case OneWay:
                sourceHandler = source.registerPropertyChanged(onSourceChanged, null);
                break;
            case OneWayToSource:
                destinationHandler = destination.registerPropertyChanged(onDestinationChanged, null);
                break;
            case TwoWay:
                sourceHandler = source.registerPropertyChanged(onSourceChanged, null);
                destinationHandler = destination.registerPropertyChanged(onDestinationChanged, null);
                break;
        }
    }

    /**
     * Activates the data binding and propagates the source to the destination
     */
    public DataBinding activate() {
        activated = true;

        log("activation");

        onSourceChanged.exec(null, null);

        return this;
    }

    /**
     * Suspend the data binding. Can be reactivated with {@link #activate}
     */
    public DataBinding suspend() {
        activated = false;

        log("suspended");

        return this;
    }

    /**
     * Terminates the Data Binding activation and cleans up all related
     * resources. You should call this method when you want to free the binding,
     * in order to lower memory usage.
     */
    public void terminate() {
        log("terminate");

        activated = false;
        converter = null;

        if (source != null && sourceHandler != null) {
            source.removePropertyChangedHandler(sourceHandler);
        }

        if (destination != null && destinationHandler != null) {
            destination.removePropertyChangedHandler(destinationHandler);
        }

        source = null;
        sourceHandler = null;

        destination = null;
        destinationHandler = null;
    }

    protected void log(String text) {
        if (logPrefix == null) {
            return;
        }

        logger.info("DATABINDING " + logPrefix + " : " + text);
    }
}
