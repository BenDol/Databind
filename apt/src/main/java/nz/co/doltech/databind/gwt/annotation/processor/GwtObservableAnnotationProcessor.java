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
package nz.co.doltech.databind.gwt.annotation.processor;

import nz.co.doltech.databind.annotation.processor.ObservableAnnotationProcessor;
import nz.co.doltech.databind.annotation.processor.Template;
import nz.co.doltech.databind.gwt.annotation.Observable;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.lang.annotation.Annotation;

@SupportedAnnotationTypes({
    "nz.co.doltech.databind.gwt.annotation.Observable"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class GwtObservableAnnotationProcessor extends ObservableAnnotationProcessor {
    private final static String TEMPLATE_CLASS = "nz/co/doltech/databind/gwt/annotation/processor/TemplateClass.txt";

    @Override
    protected String generateExtraImports(ProcInfo procInfo) {
        String extraImports = super.generateExtraImports(procInfo);
        if (!extraImports.contains("nz.co.doltech.databind.classinfo.gwt.ClazzBundle")) {
            extraImports += "import nz.co.doltech.databind.classinfo.gwt.ClazzBundle;\n";
        }
        if (!extraImports.contains("nz.co.doltech.databind.classinfo.gwt.ReflectedClasses")) {
            extraImports += "import nz.co.doltech.databind.classinfo.gwt.ReflectedClasses;\n";
        }
        return extraImports;
    }

    @Override
    protected StringBuilder generateClassEntry(ProcInfo procInfo) {
        return super.generateClassEntry(procInfo)
            .append(Template.fromResource(TEMPLATE_CLASS, BEGIN_INDEX).toString());
    }

    @Override
    protected int getInheritDepth(Annotation annotation) {
        if (annotation instanceof Observable) {
            Observable observable = ((Observable) annotation);
            int depth = observable.inheritDepth();
            return observable.inherit() ? depth : (depth != Observable.INHERIT_MAX ? depth : 0);
        }
        return -1;
    }

    @Override
    protected boolean canUseCopyConstructor(Annotation annotation) {
        return annotation instanceof Observable && ((Observable) annotation).copyConstructor();
    }
}
