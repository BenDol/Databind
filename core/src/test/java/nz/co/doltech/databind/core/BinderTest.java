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

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import nz.co.doltech.databind.core.dto.A;
import nz.co.doltech.databind.core.dto.ANotif;
import nz.co.doltech.databind.core.dto.B;
import nz.co.doltech.databind.core.properties.Properties;
import nz.co.doltech.databind.core.dto.BNotif;

public class BinderTest extends TestCase {
    public void test001() {
        A a = new A();
        B b = new B();

        a.firstName = "Hello";

        Binder.bind(a, "firstName").to(b, "firstName");

        assertEquals("Hello", b.firstName);
    }

    public void test002() {
        ANotif source = new ANotif();
        BNotif destination = new BNotif();

        // Binds the source and destination with default options (bidirectional)
        Binder.bind(source, "firstName").to(destination, "firstName");

        // Check source to destination
        String value = "Hello";
        source.setFirstName(value);
        assertEquals(value, destination.getFirstName());

        // Check destination to source
        value = "Hi !";
        destination.setFirstName(value);
        assertEquals(value, source.getFirstName());
    }

    public void test003() {
        List<String> list = Arrays.asList("zero", "one", "two", "three", "four");

        Properties.setValue(list, "selected", 3);
        assertEquals(3, Properties.getValue(list, "selected"));
    }

    public void test004() {
        ANotif source = new ANotif();
        BNotif destination = new BNotif();

        // Binds the source and destination
        Binder.map(source, destination);

        // Check the firstName field

        // Check source to destination
        String value = "Hello";
        source.setFirstName(value);
        assertEquals(value, destination.getFirstName());

        // Check destination to source
        value = "Hi !";
        destination.setFirstName(value);
        assertEquals(value, source.getFirstName());

        // Check the lastName field

        // Check source to destination
        value = "Lasty";
        source.setLastName(value);
        assertEquals(value, destination.getLastName());

        // Check destination to source
        value = "Toooooss";
        destination.setLastName(value);
        assertEquals(value, source.getLastName());
    }
}