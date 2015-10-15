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
package nz.co.doltech.databind.reflect.gwt.internal;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;

import nz.co.doltech.databind.reflect.Clazz;
import nz.co.doltech.databind.reflect.Field;

public class JavaScriptObjectClazz implements Clazz<JavaScriptObject> {
    private static HashMap<String, Field> fields = new HashMap<>();

    @Override
    public String getClassName() {
        return "JavaScriptObject";
    }

    @Override
    public Class<JavaScriptObject> getReflectedClass() {
        return JavaScriptObject.class;
    }

    @Override
    public List<Field> getFields() {
        return null;
    }

    private native void setJsoProperty(JavaScriptObject jso, String property, Object value)
    /*-{
        jso[property] = value;
    }-*/;

    private native <T> T getJsoProperty(JavaScriptObject jso, String property)
	/*-{
        return jso[property] || null;
    }-*/;

    @Override
    public Field getField(final String fieldName) {
        Field res = fields.get(fieldName);
        if (res == null) {
            res = new Field() {
                @Override
                public void setValue(Object object, Object value) {
                    setJsoProperty((JavaScriptObject) object, fieldName, value);
                }

                @Override
                public <OUT> OUT getValue(Object object) {
                    return getJsoProperty((JavaScriptObject) object, fieldName);
                }

                @Override
                public Class<?> getType() {
                    return null;
                }

                @Override
                public String getName() {
                    return fieldName;
                }

                @Override
                public void copyValueTo(Object source, Object destination) {
                    throw new RuntimeException("Not yet implemented");
                }

                @Override
                public int getModifier() {
                    return 0;
                }
            };
            fields.put(fieldName, res);
        }
        return res;
    }

    @Override
    public JavaScriptObject newInstance() {
        throw new RuntimeException("Cannot create a JavaScriptObject");
    }

    @Override
    public Clazz<? super JavaScriptObject> getSuperclass() {
        return null;
    }

    @Override
    public List<Field> getDeclaredFields() {
        return null;
    }

    @Override
    public Field getDeclaredField(String fieldName) {
        return getField(fieldName);
    }

    @Override
    public List<Field> getAllFields() {
        return null;
    }

    @Override
    public Field getAllField(String fieldName) {
        return getField(fieldName);
    }

}
