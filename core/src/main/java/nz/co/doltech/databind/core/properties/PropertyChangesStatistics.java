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
package nz.co.doltech.databind.core.properties;

import java.util.HashMap;
import java.util.Map.Entry;

class PropertyChangesStatistics {
    private static int nbRegisteredHandlers = 0;
    private static int nbNotifications = 0;
    private static int nbDispatches = 0;
    private static HashMap<String, Integer> counts = new HashMap<>();
    private static HashMap<String, Integer> oldCounts = new HashMap<>();

    /**
     * Show an alert containing useful information for debugging. It also
     * shows how many registrations happened since last call ; that's useful
     * to detect registration leaks.
     */
    String getStatistics() {
        String msg = "PropertyChanges stats :\r\n"
            + "# registered handlers : " + nbRegisteredHandlers + "\r\n"
            + "# notifications       : " + nbNotifications + "\r\n"
            + "# dispatches          : " + nbDispatches + "\r\n";

        StringBuilder details = new StringBuilder();
        for (Entry<String, Integer> e : counts.entrySet()) {
            details.append(e.getKey() + " => " + e.getValue());

            Integer oldCount = oldCounts.get(e.getKey());
            if (oldCount != null)
                details.append(" (diff: " + (e.getValue() - oldCount) + ")");

            details.append("\n");
        }

        oldCounts = new HashMap<>(counts);

        return msg + details.toString();
    }

    void addNotification() {
        nbNotifications++;
    }

    void addDispatch() {
        nbDispatches++;
    }

    void statsAddedRegistration(PropertyChanges.HandlerInfo info) {
        nbRegisteredHandlers++;

        String key = info.propertyName + "@" + info.source.getClass().getSimpleName();
        Integer count = counts.get(key);
        if (count == null)
            count = 0;
        count++;
        counts.put(key, count);
    }

    void statsRemovedRegistration(PropertyChanges.HandlerInfo info) {
        nbRegisteredHandlers--;

        String key = info.propertyName + "@" + info.source.getClass().getSimpleName();
        counts.put(key, counts.get(key) - 1);
    }
}
