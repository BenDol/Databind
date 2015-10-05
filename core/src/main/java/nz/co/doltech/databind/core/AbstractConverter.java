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

public abstract class AbstractConverter<A, B> implements BidirectionalConverter<A, B> {

    private final Class<A> aClass;
    private final Class<B> bClass;

    public AbstractConverter(Class<A> aClass, Class<B> bClass) {
        this.aClass = aClass;
        this.bClass = bClass;
    }

    /**
     * Try determine if this converter can convert the given types
     * and return the appropriate converter (in the case of reversals).
     */
    public Converter determine(Class<?> aType, Class<?> bType) {
        if (aType.equals(aClass) && bType.equals(bClass)) {
            return this;
        } else if (aType.equals(bClass) && bType.equals(aClass)) {
            return reverse();
        }
        return null;
    }

    @Override
    public Converter<B, A> reverse() {
        return new AbstractConverter<B, A>(bClass, aClass) {
            @Override
            public A convert(B value) {
                return AbstractConverter.this.convertBack(value);
            }

            @Override
            public B convertBack(A value) {
                return AbstractConverter.this.convert(value);
            }
        };
    }
}
