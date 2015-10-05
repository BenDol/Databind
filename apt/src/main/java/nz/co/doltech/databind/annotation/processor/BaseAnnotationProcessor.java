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

import nz.co.doltech.databind.annotation.processor.modules.ProcessorModule;

import javax.annotation.processing.*;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class BaseAnnotationProcessor extends AbstractProcessor {

    protected Elements elementUtils;
    protected Types typeUtils;
    protected Filer filer;
    protected Types types;
    protected Messager msg;
    protected TypeSimplifier typeSimplifier;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        msg = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        typeSimplifier = new TypeSimplifier(types);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean process(Set<? extends TypeElement> arg0, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            for (String supported : getSupportedAnnotationTypes()) {
                try {
                    Class<? extends Annotation> annotationClazz = (Class<? extends Annotation>) Class.forName(supported);
                    for (final TypeElement typeElement : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(annotationClazz))) {
                        Annotation annotation = typeElement.getAnnotation(annotationClazz);
                        String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
                        String targetTypeName = getTargetTypeName(typeElement);

                        try {
                            JavaFileObject jfo = filer.createSourceFile(packageName + "." + targetTypeName);
                            Writer writer = jfo.openWriter();

                            // Start processing
                            doProcess(new ProcInfo(typeElement, annotation, targetTypeName,
                                packageName), writer);

                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            error(e.getMessage());
                        }
                    }
                } catch (ClassNotFoundException | ClassCastException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return true;
    }

    protected abstract void doProcess(ProcInfo procInfo, Writer writer) throws IOException;

    protected String getTargetTypeName(TypeElement typeElement) {
        return typeElement.getSimpleName().toString().replace(".", "_") + "Impl";
    }

    protected final void parseGeneralTags(Template template, ProcInfo procInfo) {
        String sourceName = procInfo.typeElement.getSimpleName().toString();

        template.replace(Tags.PACKAGE_NAME, procInfo.packageName);
        template.replace(Tags.SOURCE_CLASS_FQN, procInfo.typeElement.getQualifiedName().toString());
        template.replace(Tags.SOURCE_CLASS_NAME, sourceName + TypeSimplifier.actualTypeParametersString(procInfo.typeElement));
        template.replace(Tags.TARGET_CLASS_PARAMETRIZED, procInfo.implName + typeSimplifier.formalTypeParametersString(procInfo.typeElement));
        template.replace(Tags.TARGET_CLASS_NAME, procInfo.implName);
    }

    protected void registerProcessorModule(ProcessorModule module) {
        ProcessorModuleRegistry.register(module);
    }

    protected List<ProcessorModule> getProcessorModules() {
        return ProcessorModuleRegistry.getProcessorModules();
    }

    protected void error(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }

    public static class ProcInfo {
        TypeElement typeElement;
        Annotation annotation;
        String implName;
        String packageName;
        List<String> extraImports;

        public ProcInfo(TypeElement typeElement, Annotation annotation,
                        String implName, String packageName) {
            this.typeElement = typeElement;
            this.annotation = annotation;
            this.implName = implName;
            this.packageName = packageName;
            extraImports = new ArrayList<>();
        }

        public TypeElement getTypeElement() {
            return typeElement;
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public String getImplName() {
            return implName;
        }

        public String getPackageName() {
            return packageName;
        }

        public List<String> getExtraImports() {
            return extraImports;
        }

        public void addExtraImport(String newImport) {
            extraImports.add(newImport);
        }
    }
}
