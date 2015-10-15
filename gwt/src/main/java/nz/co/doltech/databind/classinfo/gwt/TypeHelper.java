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
import nz.co.doltech.databind.util.MethodHelper;

public class TypeHelper {

    public static <T> boolean isInstanceOf(JClassType classType, Class<T> interfaceType) {
        for (JClassType type : classType.getImplementedInterfaces()) {
            if (type.getQualifiedSourceName().equals(interfaceType.getName())) {
                return true;
            }
        }
        return false;
    }

    public static String stripSetterOrGetterPrefix(JMethod method) {
        return MethodHelper.stripSetterOrGetterPrefix(method.getName());
    }
}
