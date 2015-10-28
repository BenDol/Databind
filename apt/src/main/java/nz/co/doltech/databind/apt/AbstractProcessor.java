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
package nz.co.doltech.databind.apt;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nz.co.doltech.databind.apt.guice.ProcessorModule;
import nz.co.doltech.databind.util.StringUtils;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
public abstract class AbstractProcessor extends javax.annotation.processing.AbstractProcessor {

    private final Logger logger;
    private final AbstractProcessorModule module;

    private Injector injector;
    private ProcessorInfo procInfo;

    public AbstractProcessor(Logger logger) {
        this(logger, new ProcessorModule());
    }

    public AbstractProcessor(Logger logger, AbstractProcessorModule module) {
        this.logger = logger;
        this.module = module;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        module.setProcessingEnv(processingEnv);
        injector = Guice.createInjector(module);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            for (String supported : getSupportedAnnotationTypes()) {
                try {
                    Class<? extends Annotation> aClass = (Class<? extends Annotation>) Class.forName(supported);
                    Set<TypeElement> typeElements = ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(aClass));

                    for (final TypeElement typeElement : typeElements) {
                        try {
                            Annotation annotation = typeElement.getAnnotation(aClass);
                            String packageName = getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
                            String targetTypeName = getTargetTypeName(typeElement);

                            // Start processing
                            doProcess(new ProcessorInfo(typeElement, annotation, targetTypeName, packageName));
                        }
                        catch (IOException e) {
                            error(e.getMessage());
                        }
                    }
                } catch (ClassNotFoundException | ClassCastException ex) {
                    logger.log(Level.SEVERE, "Processing failed", ex);
                }
            }
        }
        return doFinalize();
    }

    protected abstract void doProcess(ProcessorInfo procInfo) throws IOException;

    protected boolean doFinalize() {
        return true; // Nothing by default
    }

    public ProcessorInfo getProcInfo() {
        return procInfo;
    }

    public Elements getElementUtils() {
        return injector.getInstance(Elements.class);
    }

    public Types getTypeUtils() {
        return injector.getInstance(Types.class);
    }

    public Filer getFiler() {
        return injector.getInstance(Filer.class);
    }

    public Messager getMessager() {
        return injector.getInstance(Messager.class);
    }

    public TypeSimplifier getTypeSimplifier() {
        return injector.getInstance(TypeSimplifier.class);
    }

    public Injector getInjector() {
        return injector;
    }

    protected String getTargetTypeName(TypeElement typeElement) {
        return typeElement.getSimpleName().toString() + "Impl";
    }

    protected void error(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }

    private ExecutableElement getFieldSetterMethod(TypeElement typeElement, String fieldName) {
        return getMethodForField(typeElement, fieldName, "set");
    }

    private ExecutableElement getFieldGetterMethod(TypeElement typeElement, String fieldName) {
        return getMethodForField(typeElement, fieldName, "get", "is");
    }

    private ExecutableElement getMethodForField(TypeElement typeElement, String fieldName, String... startsWith) {
        for (ExecutableElement method : ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
            if (method.getModifiers().contains(Modifier.PRIVATE)) {
                continue;
            }

            String methodName = method.getSimpleName().toString();
            for (String prefix : startsWith) {
                if (methodName.startsWith(prefix) && Character.isUpperCase(methodName.charAt(prefix.length()))) {
                    String propName = StringUtils.lowerFirstLetter(methodName.substring(prefix.length()));
                    if (!propName.equals(fieldName))
                        continue;

                    return method;
                }
            }
        }
        return null;
    }

    protected boolean isFieldAccessible(VariableElement field, ProcessorInfo procInfo) {
        Element enclosingType = field.getEnclosingElement();

        // Check for matching package to ensure type
        // protected accessibility must be package enclosed
        String typePkg = getElementUtils().getPackageOf(enclosingType).getQualifiedName().toString();
        boolean foreignEnclosedType = !typePkg.equals(procInfo.packageName);

        // Private fields are inaccessible, protected
        // fields must not be foreign fields
        boolean isPrivate = field.getModifiers().contains(Modifier.PRIVATE);
        boolean isProtected = field.getModifiers().contains(Modifier.PROTECTED)
            || !field.getModifiers().contains(Modifier.PUBLIC);

        // Check field access conditions
        return !isPrivate && (!isProtected || !foreignEnclosedType);
    }
}
