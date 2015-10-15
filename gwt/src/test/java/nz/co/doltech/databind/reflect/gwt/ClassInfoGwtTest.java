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
package nz.co.doltech.databind.reflect.gwt;

import com.google.gwt.junit.client.GWTTestCase;

import nz.co.doltech.databind.reflect.Reflection;
import nz.co.doltech.databind.reflect.Clazz;

public class ClassInfoGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "nz.co.doltech.databind.reflect.ReflectionTest";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();

    }

    public void testRegisteredClazz() {
        Clazz<?> clz = Reflection.findClazz(A.class);
        assertNotNull(clz);
    }

    public void testFields() {
        Clazz<?> clz = Reflection.findClazz(A.class);
        assertEquals(clz.getAllFields().size(), 2);
    }
}