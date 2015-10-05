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

/**
 * Interface for converting a value between two types. Those two types are not specified
 * at compile time and will be discovered at runtime.
 *
 * @author Ben Dol
 */
public interface BidirectionalConverter<A, B> extends Converter<A, B> {
    /**
     * Get the reversed version of the converter.
     */
    Converter<B, A> reverse();
}
