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
 * To be used with {@link ReflectedClasses} annotation
 * <p/>
 * When you inheritDepth this interface, declare a void register() method
 * annotated with {@link ReflectedClasses}.<br/>
 * Call this method to register the use of reflection
 * on listed classes
 * <p/>
 * Example:<br/>
 * <pre>
 * // Declare your Bundle :
 * interface MyBundle extends ClazzBundle
 * {
 *    &#64;ReflectedClasses(
 *      classes = {
 *          MyDTOClass.class,
 *          TextBox.class,
 *          JavaScriptObject.class,
 *          ...
 *      })
 *      void register();
 * }
 *
 * // Register it
 * MyBundle bundle = GWT.create( MyBundle.class );
 * bundle.register();
 *
 * // Now the DataBinding engine can use those classes.
 * // You can also use them through ClazzInfo :
 * clazz<MyDTOClass> clazz = ClassInfo.clazz( MyDTOClass.class );
 * clazz.getMethods(); // and other reflection methods...
 * </pre>
 */
public interface ClazzBundle {
    void register();
}
