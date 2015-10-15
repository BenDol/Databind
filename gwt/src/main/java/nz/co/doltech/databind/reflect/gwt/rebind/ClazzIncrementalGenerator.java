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
package nz.co.doltech.databind.reflect.gwt.rebind;

import com.google.gwt.core.ext.*;

public class ClazzIncrementalGenerator extends IncrementalGenerator {

    private static final int VERSION = 140;

    private ClazzGenerator generator;

    @Override
    public RebindResult generateIncrementally(TreeLogger logger, GeneratorContext context, String typeName)
            throws UnableToCompleteException {
        if(generator == null) {
            generator = new ClazzGenerator();
        }

        String generatedClassName = generator.generate(logger, context, typeName);
        return new RebindResult(RebindMode.USE_ALL_NEW, generatedClassName);
    }

    @Override
    public long getVersionId() {
        return VERSION;
    }
}