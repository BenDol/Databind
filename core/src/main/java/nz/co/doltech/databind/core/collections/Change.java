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
package nz.co.doltech.databind.core.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Change {
    final ChangeType type;
    final Object item;
    final int index;

    public Change(ChangeType type, Object item, int index) {
        this.type = type;
        this.item = item;
        this.index = index;
    }

    public static <T> List<Change> ForItems(ChangeType type, Collection<T> items, int startIndex) {
        List<Change> res = new ArrayList<>();
        for (T item : items) {
            res.add(new Change(type, item, startIndex++));
        }

        return res;
    }

    public ChangeType getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public <T> T getItem() {
        return (T) item;
    }

    public int getIndex() {
        return index;
    }
}