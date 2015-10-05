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
package nz.co.doltech.databind.core.converters;

import nz.co.doltech.databind.core.Converter;

/**
 * Converter for enum types.
 *
 * @author Ben Dol
 */
public class EnumConverter implements Converter {

    private Enum[] values;

    private boolean toOrdinal;

    /**
     * Default constructor, won't use the enums ordinal value by default.
     *
     * @see #EnumConverter(Enum[] values, boolean toOrdinal)
     */
    public EnumConverter(Enum[] values) {
        this(values, false);
    }

    /**
     * Constructor to define whether or not the enum should be used as its
     * ordinal value, otherwise it will simply call toString on the enum.
     */
    public EnumConverter(Enum[] values, boolean toOrdinal) {
        this.values = values;
        this.toOrdinal = toOrdinal;
    }

    @Override
    public Object convert(Object value) {
        if (toOrdinal) {
            for (int i = 0; i < values.length; ++i) {
                if (values[i].equals(value))
                    return i;
            }
        }
        return value.toString();
    }

    @Override
    public Object convertBack(Object value) {
        Enum e = null;
        try {
            if (value instanceof Integer) {
                e = values[(Integer) value];
            } else if (value instanceof String) {
                try {
                    // Find by ordinal value first
                    e = values[Integer.parseInt((String) value)];
                } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                    // Failed to find by ordinal value
                    // Try find by enum name string
                    for (Enum val : values) {
                        if (val.name().equals(value)) {
                            e = val;
                            break;
                        }
                    }
                    assert e != null;
                }
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Attempted to convert an " +
                "enum using an invalid value: ", ex);
        }
        return e;
    }
}