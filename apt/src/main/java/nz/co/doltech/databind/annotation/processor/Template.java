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
package nz.co.doltech.databind.annotation.processor;

import java.io.InputStream;
import java.util.Scanner;

public class Template {
    private String value;

    public static Template fromResource(String path) {
        Template result = new Template();

        result.value = readResource(path);

        return result;
    }

    public static Template fromResource(String path, int index) {
        Template result = new Template();

        result.value = readResource(path, index);

        return result;
    }

    private static String readResource(String path) {
        InputStream is = Template.class.getClassLoader().getResourceAsStream(path);

        Scanner s = new Scanner(is);
        s.useDelimiter("\\A");

        String result = s.hasNext() ? s.next() : "";

        s.close();
        return result;
    }

    private static String readResource(String path, int index) {
        InputStream is = Template.class.getClassLoader().getResourceAsStream(path);

        if (is == null)
            throw new RuntimeException("Not found resource " + path);

        Scanner s = new Scanner(is);
        s.useDelimiter("------");

        String result;
        int i = -1;
        do {
            if (!s.hasNext()) {
                result = "";
                break;
            }

            result = s.next();
            i++;
        }
        while (i < index);

        s.close();

        return result;
    }

    public Template replace(String target, String replacement) {
        value = value.replace(target, replacement);
        return this;
    }

    @Override
    public String toString() {
        return value;
    }
}
