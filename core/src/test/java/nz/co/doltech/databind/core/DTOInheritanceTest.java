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

import junit.framework.TestCase;
import nz.co.doltech.databind.core.properties.Properties;

public class DTOInheritanceTest extends TestCase {
    public void test() {
        A a = new SubA();
        A b = new SubA();

        Binder.bind(a, "firstName").to(b, "firstName");

        a.setFirstName("titi");
        assertEquals("titi", b.getFirstName());

        b.setFirstName("tata");
        assertEquals("tata", a.getFirstName());
    }

    class A {
        private String firstName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }

    class SubA extends A {
        public void setFirstName(String firstName) {
            super.setFirstName(firstName);

            Properties.notify(this, "firstName");
        }
    }
}