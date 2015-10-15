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
package nz.co.doltech.databind.core.propertyadapters;

import java.util.ArrayList;

import nz.co.doltech.databind.core.PlatformSpecificProvider;
import nz.co.doltech.databind.util.Action1;

public class CompositePropertyAdapter implements PropertyAdapter {
    public final static String HASVALUE_TOKEN = "$HasValue";
    public final static String MODELMAP_TOKEN = "$ModelMap";

    private Object context;
    private String[] path;

    private PropertyAdapter[] adapters;
    private Object[] adapterHandlerRegistrations;

    private ArrayList<ClientInfo> clients;

    private Action1<PropertyAdapter, Object> onPropertyChanged = new Action1<PropertyAdapter, Object>() {
        @Override
        public void exec(PropertyAdapter p1, Object p2) {
            int adapterNo = (Integer) p2;

            // unregister all adapters with a position > adapterNo
            for (int p = adapterNo + 1; p < path.length; p++) {
                if (adapters[p] == null || adapterHandlerRegistrations[p] == null)
                    continue;

                adapters[p].removePropertyChangedHandler(adapterHandlerRegistrations[p]);
                adapters[p] = null;
                adapterHandlerRegistrations[p] = null;
            }

            // signal callbacks that a change occurred
            if (clients != null) {
                for (ClientInfo client : clients)
                    client.callback.exec(CompositePropertyAdapter.this, client.cookie);
            }
        }
    };

    public CompositePropertyAdapter(Object context, String path) {
        this.context = context;
        this.path = path.split("\\.");

        adapters = new PropertyAdapter[this.path.length];
        adapterHandlerRegistrations = new Object[this.path.length];
    }

    @Override
    public Object getValue() {
        tryCreateAdapters();

        if (adapters[path.length - 1] != null) {
            return adapters[path.length - 1].getValue();
        }
        return null;
    }

    @Override
    public void setValue(Object object) {
        tryCreateAdapters();

        if (adapters[path.length - 1] != null) {
            adapters[path.length - 1].setValue(object);
        }
    }

    @Override
    public Object registerPropertyChanged(Action1<PropertyAdapter, Object> callback, Object cookie) {
        tryCreateAdapters();

        if (clients == null) {
            clients = new ArrayList<>();
        }

        ClientInfo client = new ClientInfo();
        client.callback = callback;
        client.cookie = cookie;

        // what if any sub path contains a property that's not subscribable ?

        clients.add(client);
        return client;
    }

    @Override
    public void removePropertyChangedHandler(Object handlerRegistration) {
        ClientInfo client = (ClientInfo) handlerRegistration;
        client.callback = null;
        client.cookie = null;

        clients.remove(client);
        if (clients.isEmpty()) {
            clients = null;
        }

        // remove adapters
        for (int i = 0; i < adapters.length; i++) {
            if (adapters[i] == null) {
                continue;
            }
            adapters[i].removePropertyChangedHandler(adapterHandlerRegistrations[i]);
        }
    }

    // create adapaters from the root context object to the end of the path, if
    // possible...
    private void tryCreateAdapters() {
        Object object = context;

        for (int p = 0; p < path.length; p++) {
            if (object == null) {
                return;
            }

            // if no adapter has yet been created for this pathItem
            if (adapters[p] == null) {
                String pathItem = path[p];

                // try to find an adapter, otherwise create one or return null
                // to create an adapter, we need a context and a path item
                // context is the 'object' value (ie the value of the previous
                // pathItem or the root context)
                // path item is path[p]
                if (pathItem.charAt(0) == '$') {
                    if (PlatformSpecificProvider.get().isBindingToken(pathItem)) {
                        adapters[p] = PlatformSpecificProvider.get().createPropertyAdapter(object);
                    }
                    else if (CompositePropertyAdapter.MODELMAP_TOKEN.equals(pathItem)) {
                        adapters[p] = new MapperPropertyAdapter(object);
                    }
                } else {
                    adapters[p] = new ObjectPropertyAdapter(object, pathItem);
                }

                // we should subscribe to the value changes so that we can
                // subscribe to
                // new values when anything on the path changes
                adapterHandlerRegistrations[p] = adapters[p].registerPropertyChanged(onPropertyChanged, p);
            }

            if (p < path.length - 1) {
                object = adapters[p].getValue();
            }
        }
    }

    class ClientInfo {
        Action1<PropertyAdapter, Object> callback;
        Object cookie;
    }
}
