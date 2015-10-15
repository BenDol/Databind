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

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

public class ClazzGenerator extends Generator {
    // Context and logger for code generation
    TreeLogger logger;
    GeneratorContext context;

    // asked type name
    String askedTypeName;

    // type for which we provide information
    JClassType reflectedType;

    // generated class name
    String generatedClassName;

    private JClassType getReflectedType(TypeOracle typeOracle, String askedTypeName) throws UnableToCompleteException {
        JClassType askedType;
        try {
            askedType = typeOracle.getType(askedTypeName);
        } catch (NotFoundException e) {
            throw new UnableToCompleteException();
        }

        for (JClassType classType : askedType.getImplementedInterfaces()) {
            if (!classType.getQualifiedSourceName().equals("nz.co.doltech.databind.classinfo.Clazz"))
                continue;

            JParameterizedType parametrized = classType.isParameterized();
            JClassType[] typeArgs = parametrized.getTypeArgs();

            return typeArgs[0];
        }

        throw new UnableToCompleteException();
    }

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
        this.logger = logger;
        this.context = context;
        this.askedTypeName = typeName;

        // get the "reflection" machine of GWT compiler
        TypeOracle typeOracle = context.getTypeOracle();
        try {
            reflectedType = getReflectedType(typeOracle, typeName);
            ClazzInfoBuilder builder = new ClazzInfoBuilder(logger, context);

            return builder.buildClassInfoFor(reflectedType);
        } catch (Exception e) {
            // record to logger that Map generation threw an exception
            logger.log(TreeLogger.ERROR, "ERROR when generating " + generatedClassName + " for " + typeName, e);
            return null;
        }
    }
}