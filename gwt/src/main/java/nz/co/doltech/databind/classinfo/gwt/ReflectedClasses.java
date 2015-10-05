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

/**
 * Describes a list of classes for which introspection will be enabled
 * at runtime.
 * <p/>
 * This annotation should be used on the register() method of the
 * {@link ClazzBundle} interface.
 *
 * @author Arnaud
 */
public @interface ReflectedClasses {
    Class<?>[] classes() default {};
    Class<?>[] ignored() default {};
}
