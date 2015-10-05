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
package nz.co.doltech.databind.gwt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation trigger the generation of a child
 * class with all the required getters and setters to
 * be considered as "Observable" :<br/><br/>
 * <p/>
 * - all glue code for generating runtime type information for GWT<br/>
 * - there is a getter for each non-private field of the class<br/>
 * - there is a setter for each of the non-private field of the
 * class.<br/><br/>
 * <p/>
 * Each setter should trigger an event on the binding system.<br/><br/>
 * <p/>
 * The generated class will be generated in the same package
 * as the annotated class and will have the same name, prefixed
 * with "Observable".<br/><br/>
 * <p/>
 * If the annotated class name finishes by "Internal", then the
 * generated class name will have the same name without "Internal".<br/><br/>
 * <p/>
 * So if this annotation is put on a nz.co.doltech.Person class,
 * it will generate an observable implementation with the following
 * fqn : nz.co.doltech.ObservablePerson<br/><br/>
 *
 * @author Arnaud Tournier
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Observable {
    int INHERIT_MAX = Integer.MAX_VALUE;

    /**
     * Generate copy constructor, if possible.
     */
    boolean copyConstructor() default false;

    /**
     * Should we inherit from our super class chain.<br/>
     * This will search {@link #INHERIT_MAX} by default.
     *
     * @see #inheritDepth()
     */
    boolean inherit() default false;

    /**
     * Max depth to inheritDepth from superclasses.<br/>
     * If this is customized it will set {@link #inherit()} to true.
     */
    int inheritDepth() default INHERIT_MAX;
}
