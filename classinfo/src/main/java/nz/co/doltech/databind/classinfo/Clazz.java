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
package nz.co.doltech.databind.classinfo;

import java.util.List;

/**
 * Runtime type information interface about a class.
 */
public interface Clazz<T> {
    String getClassName();

    Class<T> getReflectedClass();

    Clazz<? super T> getSuperclass();

    List<Field> getAllFields();

    Field getAllField(String fieldName);

    List<Field> getFields();

    Field getField(String fieldName);

    List<Field> getDeclaredFields();

    Field getDeclaredField(String fieldName);

    List<Method> getMethods();

    Method getMethod(String methodName);

    T newInstance();
}
