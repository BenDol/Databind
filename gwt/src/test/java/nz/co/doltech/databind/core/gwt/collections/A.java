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

import nz.co.doltech.databind.core.properties.Properties;
import nz.co.doltech.databind.core.collections.WatchableCollection;

public class A {
    int value;

    WatchableCollection<A> children;

    public A addChild() {
        if (children == null)
            setChildren(new WatchableCollection<A>());

        A a = new A();
        children.add(a);

        return a;
    }

    public WatchableCollection<A> getChildren() {
        return children;
    }

    public void setChildren(WatchableCollection<A> children) {
        this.children = children;
        Properties.notify(this, "children");
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        Properties.notify(this, "value");
    }
}