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
import nz.co.doltech.databind.core.dto.A;
import nz.co.doltech.databind.core.dto.ANotif;
import nz.co.doltech.databind.core.properties.Properties;

public class DTOInheritanceTest extends TestCase {
    public void test() {
        A a = new ANotif();
        A b = new ANotif();

        Binder.bind(a, "firstName").to(b, "firstName");

        a.setFirstName("titi");
        assertEquals("titi", b.getFirstName());

        b.setFirstName("tata");
        assertEquals("tata", a.getFirstName());
    }
}