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
package nz.co.doltech.databind.core.gwt.collections;

import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;

import junit.framework.Assert;
import nz.co.doltech.databind.core.gwt.Binder;
import nz.co.doltech.databind.core.collections.WatchableCollection;
import nz.co.doltech.databind.reflect.ReflectionRegistry;
import nz.co.doltech.databind.reflect.Reflections;
import nz.co.doltech.databind.util.Action;
import nz.co.doltech.databind.core.PlatformSpecificProvider;
import nz.co.doltech.databind.core.collections.Change;

public class WatchableCollectionGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "nz.co.doltech.databind.core.DatabindTest";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        ReflectionRegistry.register();
    }

    public void testA() {
        WatchableCollection<A> collection = new WatchableCollection<>();
        assertEquals(collection, collection);

        delayTestFinish(500);

        collection.addCallback(new Action<List<Change>>() {
            @Override
            public void exec(List<Change> param) {
                finishTest();
            }
        });

        collection.add(new A());
    }

    public void testC() {
        A a = new A();

        PlatformSpecificProvider.get().setObjectMetadata(a, this);
        Assert.assertEquals(this, PlatformSpecificProvider.get().getObjectMetadata(a));
    }

    public void testB() {
        A a = new A();
        A b = new A();

        Binder.bind(a, "value").to(b, "value");
        Binder.bind(a, "children").to(b, "children");

        a.setValue(55);
        b.setChildren(new WatchableCollection<A>());

        delayTestFinish(500);

        assertEquals(a.getChildren(), b.getChildren());
        a.getChildren().addCallback(new Action<List<Change>>() {
            @Override
            public void exec(List<Change> param) {
                finishTest();
            }
        });

        b.addChild();

        assertEquals(a.getValue(), b.getValue());
        assertEquals(a.getValue(), 55);
    }
}
