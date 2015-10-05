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

public class TestUtils {
    private static String[] names = {"John", "Maria", "Cassandra", "Aurora", "Klert", "Minau", "Herfva", "Orpia", "Caronin", "Tart"};
    private static String[] colors = {"red", "blue", "white", "black", "green", "pink", "grey", "yellow"};

    public static String randomName() {
        return names[(int) (Math.random() * names.length)] + " " + names[(int) (Math.random() * names.length)];
    }

    public static String randomColor() {
        return colors[(int) (Math.random() * colors.length)];
    }
}
