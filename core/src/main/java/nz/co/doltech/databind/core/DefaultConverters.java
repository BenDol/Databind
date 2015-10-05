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

import nz.co.doltech.databind.core.converters.StringDoubleConverter;
import nz.co.doltech.databind.core.converters.StringFloatConverter;
import nz.co.doltech.databind.core.converters.StringIntegerConverter;
import nz.co.doltech.databind.core.converters.StringLongConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of implementations of standard converters.
 *
 * @author Ben Dol
 */
public class DefaultConverters {

    private static List<AbstractConverter<?, ?>> converters = new ArrayList<>();

    static {
        converters.add(new StringIntegerConverter());
        converters.add(new StringDoubleConverter());
        converters.add(new StringFloatConverter());
        converters.add(new StringLongConverter());
    }

    /**
     * Dynamically finds the appropriate converter to use between two objects
     * of different classes.<br/>
     * Returns <code>null</code> if no appropriate converter is found.
     *
     * @param from The input class type
     * @param to   The output class type
     */
    public static Converter findConverter(Class<?> from, Class<?> to) {
        from = getBoxedType(from);
        to = getBoxedType(to);

        Converter result;
        for (AbstractConverter<?, ?> converter : converters) {
            result = converter.determine(from, to);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static Class<?> getBoxedType(Class<?> c) {
        if (c == int.class)
            return Integer.class;
        if (c == char.class)
            return Character.class;
        if (c == double.class)
            return Double.class;
        if (c == float.class)
            return Float.class;
        if (c == long.class)
            return Long.class;
        return c;
    }
}
