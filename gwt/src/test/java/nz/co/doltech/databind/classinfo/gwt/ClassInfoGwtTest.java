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

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

import nz.co.doltech.databind.classinfo.ClassInfo;
import nz.co.doltech.databind.classinfo.Clazz;

interface TestClazzBundle extends ClazzBundle {
    @ReflectedClasses(classes = {A.class})
    void register();
}

public class ClassInfoGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "nz.co.doltech.databind.classinfo.ClassInfoTest";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();

        // Registers the class introspection bundle
        GWT.<TestClazzBundle>create(TestClazzBundle.class).register();
    }

    public void testRegisteredClazz() {
        Clazz<?> clz = ClassInfo.findClazz(A.class);
        assertNotNull(clz);
    }

    public void testFields() {
        Clazz<?> clz = ClassInfo.findClazz(A.class);
        assertEquals(clz.getAllFields().size(), 2);
    }
}