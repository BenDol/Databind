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
package nz.co.doltech.databind.classinfo.gwt;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import nz.co.doltech.databind.util.StringUtils;

public class TypeHelper {

    public static final String[] GETTER_PREFIXES = new String[]{
        "get", "is"
    };

    public static final String[] SETTER_PREFIXES = new String[]{
        "set"
    };

    public static <T> boolean isInstanceOf(JClassType classType, Class<T> interfaceType) {
        for (JClassType type : classType.getImplementedInterfaces()) {
            if (type.getQualifiedSourceName().equals(interfaceType.getName())) {
                return true;
            }
        }
        return false;
    }

    public static String stripSetterOrGetterPrefix(JMethod method) {
        String result = method.getName();

        for (String prefix : GETTER_PREFIXES) {
            if (result.length() > prefix.length() && result.startsWith(prefix)
                    && Character.isUpperCase(result.charAt(prefix.length()))) {
                result = StringUtils.lowerFirstLetter(result.substring(prefix.length()));
                return result;
            }
        }

        for (String prefix : SETTER_PREFIXES) {
            if (result.length() > prefix.length() && result.startsWith(prefix)
                    && Character.isUpperCase(result.charAt(prefix.length()))) {
                result = StringUtils.lowerFirstLetter(result.substring(prefix.length()));
                return result;
            }
        }
        return result;
    }
}
