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

import nz.co.doltech.databind.classinfo.ClassInfo;
import nz.co.doltech.databind.classinfo.Clazz;
import nz.co.doltech.databind.classinfo.Field;
import nz.co.doltech.databind.classinfo.Method;
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
        List<DataBinding> res = new ArrayList<>();

        logger.fine("Binding object of class " + getSimpleName(source.getClass())
            + " to another of class " + getSimpleName(destination.getClass()));

        Clazz<?> sourceClass = ClassInfo.clazz(source.getClass());
        Clazz<?> destinationClass = ClassInfo.clazz(destination.getClass());

        // registers all possible bindings...
        HashSet<String> bindedNames = new HashSet<String>();

        // fields wise...
        for (Field field : sourceClass.getAllFields())
            bindedNames.add(field.getName());
        for (Field field : destinationClass.getAllFields())
            bindedNames.add(field.getName());

        // ... and method wise
        for (Method method : sourceClass.getMethods()) {
            if (!method.getName().startsWith("get") && !method.getName().startsWith("set"))
                continue;

            String fieldName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
            bindedNames.add(fieldName);
        }
        for (Method method : destinationClass.getMethods()) {
            if (!method.getName().startsWith("get") && !method.getName().startsWith("set"))
                continue;

            String fieldName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
            bindedNames.add(fieldName);
        }

        for (String name : bindedNames) {
            boolean srcRead = Properties.canAccessField(ClassInfo.clazz(source.getClass()), name);
            boolean srcWrite = Properties.canSetField(ClassInfo.clazz(source.getClass()), name);

            boolean destinationRead = Properties.canAccessField(ClassInfo.clazz(destination.getClass()), name);
            boolean destinationWrite = Properties.canSetField(ClassInfo.clazz(destination.getClass()), name);

            // ensure both have necessary methods or field
            if (!srcRead || !destinationWrite)
                continue; // bypass

            // adjust binding mode according to capabilities
            Mode bindingMode = Mode.OneWay;
            if (srcWrite && destinationRead)
                bindingMode = Mode.TwoWay;

            DataAdapterInfo sourceAdapterInfo = createDataAdapter(source, name, null);
            if (sourceAdapterInfo == null)
                continue;

            DataAdapterInfo destinationAdapterInfo = createDataAdapter(destination, name,
                sourceAdapterInfo.dataType);
            if (destinationAdapterInfo == null)
                continue;

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

            res.add(binding);
        }

        return res;
    }

    @SuppressWarnings("unchecked")
    public static void freeMapping(Object mappingResourceHandle) {
        List<DataBinding> bindings = (List<DataBinding>) mappingResourceHandle;
        for (DataBinding binding : bindings)
            binding.terminate();
        bindings.clear();
    }

    static String getSimpleName(Class<?> cls) {
        String[] path = cls.getName().split("\\.");
        return path[path.length - 1];
    }

    static DataAdapterInfo createDataAdapter(Object context, String property, Class<?> srcPptyType) {
        DataAdapterInfo res = new DataAdapterInfo();
        res.dataType = Properties.getPropertyType(ClassInfo.clazz(context.getClass()), property);
        res.debugString = getSimpleName(context.getClass()) + ", ";

        // test to see if the asked property is in fact a HasValue widget
        Object widget = Properties.getValue(context, property);
        if (PlatformSpecificProvider.get().isSpecificDataAdapter(widget)) {
            PlatformSpecificProvider.get().fillSpecificDataAdapter(widget, context, property, srcPptyType, res);
        } else {
            res.debugString += "\"" + property + "\"";
            res.adapter = new ObjectPropertyAdapter(context, property);
        }

        if (res.adapter == null)
            return null;

        return res;
    }
}
