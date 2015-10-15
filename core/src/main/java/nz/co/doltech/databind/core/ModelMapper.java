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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import nz.co.doltech.databind.reflect.Reflection;
import nz.co.doltech.databind.reflect.Clazz;
import nz.co.doltech.databind.reflect.Field;
import nz.co.doltech.databind.core.properties.Properties;
import nz.co.doltech.databind.core.propertyadapters.ObjectPropertyAdapter;

/**
 * A data binding utility for the support of automatic DTO binding.
 *
 * @author Arnaud Tournier
 * @author Ben Dol
 */
public class ModelMapper {
    private static final Logger logger = Logger.getLogger(ModelMapper.class.getName());

    // tries to bind as much fields of source to destination and the other way
    // around
    // returns mapping resources handle that were created for this mapping
    public static Object map(Object source, Object destination) {
        List<DataBinding> dataBindings = new ArrayList<>();

        logger.fine("Binding object of class " + getSimpleName(source.getClass())
            + " to another of class " + getSimpleName(destination.getClass()));

        Clazz<?> sourceClass = Reflection.clazz(source.getClass());
        Clazz<?> destinationClass = Reflection.clazz(destination.getClass());

        // registers all possible bindings
        HashSet<String> boundNames = new HashSet<>();

        // process the fields
        for (Field field : sourceClass.getAllFields()) {
            boundNames.add(field.getName());
        }

        for (Field field : destinationClass.getAllFields()) {
            boundNames.add(field.getName());
        }

        for (String name : boundNames) {
            boolean srcRead = Properties.canAccessField(Reflection.clazz(source.getClass()), name);
            boolean destinationWrite = Properties.canAccessField(Reflection.clazz(destination.getClass()), name);

            // ensure both have necessary fields
            if (!srcRead || !destinationWrite) {
                continue; // bypass
            }

            boolean srcWrite = Properties.canAccessField(Reflection.clazz(source.getClass()), name);
            boolean destinationRead = Properties.canAccessField(Reflection.clazz(destination.getClass()), name);

            // adjust binding mode according to capabilities
            Mode bindingMode = Mode.OneWay;
            if (srcWrite && destinationRead) {
                bindingMode = Mode.TwoWay;
            }

            DataAdapterInfo sourceAdapterInfo = createDataAdapter(source, name, null);
            if (sourceAdapterInfo == null) {
                continue;
            }

            DataAdapterInfo destinationAdapterInfo = createDataAdapter(destination, name,
                sourceAdapterInfo.dataType);
            if (destinationAdapterInfo == null) {
                continue;
            }

            // bind source, "color" <----> destination, "color.$HasValue"
            String symbol = "";
            switch (bindingMode) {
                case OneWay:
                    symbol = "----->";
                    break;
                case TwoWay:
                    symbol = "<---->";
                    break;
                case OneWayToSource:
                    symbol = "<-----";
                    break;
            }

            logger.fine("[" + getSimpleName(sourceAdapterInfo.dataType) + "] "
                + sourceAdapterInfo.debugString + symbol + destinationAdapterInfo.debugString);

            DataBinding binding = new DataBinding(sourceAdapterInfo.adapter, destinationAdapterInfo.adapter,
                bindingMode, destinationAdapterInfo.converter, null);
            binding.activate();

            dataBindings.add(binding);
        }

        return dataBindings;
    }

    @SuppressWarnings("unchecked")
    public static void freeMapping(Object mappingResourceHandle) {
        List<DataBinding> bindings = (List<DataBinding>) mappingResourceHandle;
        for (DataBinding binding : bindings) {
            binding.terminate();
        }
        bindings.clear();
    }

    static String getSimpleName(Class<?> cls) {
        String[] path = cls.getName().split("\\.");
        return path[path.length - 1];
    }

    static DataAdapterInfo createDataAdapter(Object context, String property, Class<?> srcPptyType) {
        DataAdapterInfo info = new DataAdapterInfo();
        info.dataType = Properties.getPropertyClassType(Reflection.clazz(context.getClass()), property);
        info.debugString = getSimpleName(context.getClass()) + ", ";

        // test to see if the asked property is in fact a HasValue widget
        Object value = Properties.getValue(context, property);

        PlatformSpecific platform = PlatformSpecificProvider.get();
        if (platform.isSpecificDataAdapter(value)) {
            platform.processDataAdapter(value, context, property, srcPptyType, info);
        } else {
            info.debugString += "\"" + property + "\"";
            info.adapter = new ObjectPropertyAdapter(context, property);
        }

        if (info.adapter == null) {
            return null;
        }
        return info;
    }
}
