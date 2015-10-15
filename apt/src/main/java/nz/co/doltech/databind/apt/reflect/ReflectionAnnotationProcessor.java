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
package nz.co.doltech.databind.apt.reflect;

import nz.co.doltech.databind.apt.AbstractProcessor;
import nz.co.doltech.databind.apt.ProcessorInfo;
import nz.co.doltech.databind.reflect.Reflected;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes({"nz.co.doltech.databind.reflect.Reflected"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ReflectionAnnotationProcessor extends AbstractProcessor {

    private static final Logger logger = Logger.getLogger(ReflectionAnnotationProcessor.class.getName());

    public ReflectionAnnotationProcessor() {
        super(logger, new ReflectionModule());
    }

    @Override
    protected void doProcess(ProcessorInfo procInfo) throws IOException {
        ReflectionGenerator.Factory factory = getInjector().getInstance(ReflectionGenerator.Factory.class);
        Types typeUtils = getTypeUtils();

        try {
            Reflected annotation = (Reflected)procInfo.getAnnotation();
            for(TypeMirror type : asTypeMirror(annotation)) {
                TypeElement element = (TypeElement)typeUtils.asElement(type);

                // Generate the super class
                TypeMirror superClass = element.getSuperclass();
                if(element.getSuperclass() != null) {
                    generate(superClass, factory);
                }

                generate(type, factory);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void generate(TypeMirror type, ReflectionGenerator.Factory factory) throws Exception {
        Writer writer = null;
        try {
            Element element = getTypeUtils().asElement(type);
            String fileName = element.getSimpleName().toString() + ReflectionGenerator.NAME;
            Name pkgName = getElementUtils().getPackageOf(element).getQualifiedName();

            JavaFileObject jfo = getFiler().createSourceFile(pkgName.toString() + "." + fileName);
            writer = jfo.openWriter();

            ReflectionGenerator generator = factory.createReflection(
                "nz/co/doltech/databind/apt/reflect/ReflectImpl.vm");

            generator.mergeTemplate(writer, type);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if(writer != null) {
                writer.close();
            }
        }
    }

    private List<? extends TypeMirror> asTypeMirror(Reflected reflected) {
        try {
            reflected.classes();
        }
        catch(MirroredTypesException mte) {
            return mte.getTypeMirrors();
        }
        return new ArrayList<>();
    }
}
