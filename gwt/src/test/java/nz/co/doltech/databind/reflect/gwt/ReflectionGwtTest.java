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

import nz.co.doltech.databind.reflect.ClassReflection;
import nz.co.doltech.databind.reflect.Reflected;
import nz.co.doltech.databind.reflect.ReflectionRegistry;
import nz.co.doltech.databind.reflect.Reflections;

import java.util.ArrayList;

@Reflected(classes = {
    A.class,
    ArrayList.class
})
public class ReflectionGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "nz.co.doltech.databind.reflect.ReflectionTest";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        ReflectionRegistry.register();
    }

    public void testRegisteredClazz() {
        ClassReflection<?> clz = Reflections.findClass(A.class);
        assertNotNull(clz);
    }

    public void testFields() {
        ClassReflection<?> clz = Reflections.findClass(A.class);
        assertEquals(clz.getAllFields().size(), 2);
    }
}