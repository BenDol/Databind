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

import com.github.misberner.apcommons.util.Visibility;
import nz.co.doltech.databind.apt.AbstractProcessor;
import nz.co.doltech.databind.apt.ElementUtils;
import nz.co.doltech.databind.apt.ProcessorInfo;
import nz.co.doltech.databind.apt.reflect.gwt.EmulElement;
import nz.co.doltech.databind.apt.reflect.gwt.Emulation;
import nz.co.doltech.databind.reflect.IgnoreInfo;
import nz.co.doltech.databind.reflect.Reflected;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes({"nz.co.doltech.databind.reflect.Reflected"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ReflectionAnnotationProcessor extends AbstractProcessor {

    private static final Logger logger = Logger.getLogger(ReflectionAnnotationProcessor.class.getName());

    private static final List<String> ignoredClasses = new ArrayList<>();
    static {
        ignoredClasses.add("java.lang.Object");
        ignoredClasses.add("com.google.gwt.core.client.JavaScriptObject");
    };

    private static boolean registryCreated;

    private final List<String> classCache = new ArrayList<>();

    public ReflectionAnnotationProcessor() {
        super(logger, new ReflectionModule());
    }

    @Override
    protected void doProcess(ProcessorInfo procInfo) throws IOException {
        ReflectionGenerator.Factory factory = getInjector().getInstance(ReflectionGenerator.Factory.class);

        try {
            // Class will have the Reflected annotation
            Reflected annotation = (Reflected)procInfo.getAnnotation();

            for(TypeMirror ignore : getIgnoreClasses(annotation)) {
                TypeElement element = (TypeElement)getTypeUtils().asElement(ignore);

                String name = element.getQualifiedName().toString();
                if(!ignoredClasses.contains(name)) {
                    ignoredClasses.add(name);
                }
            }

            for(TypeMirror type : getReflectClasses(annotation)) {
                generate(type, factory);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected boolean doFinalize() {
        if(registryCreated) {
            // The registry has already been created.
            // This happens when the processor is scanning
            // multiple modules.
            return true;
        }
        ReflectionGenerator.Factory factory = getInjector().getInstance(ReflectionGenerator.Factory.class);

        Writer writer = null;
        try {
            String pkgName = "nz.co.doltech.databind.reflect";

            JavaFileObject jfo = getFiler().createSourceFile("reflect." + pkgName + ".ReflectionRegistry");
            writer = jfo.openWriter();

            ReflectionRegistryGenerator generator = factory.createReflectionRegistryGenerator(
                "nz/co/doltech/databind/apt/reflect/ReflectionRegistry.vm");

            generator.mergeTemplate(writer, classCache);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error while closing the print writer.", e);
                }
            }
        }

        registryCreated = true;
        return true;
    }

    private void generate(TypeMirror type, ReflectionGenerator.Factory factory) throws Exception {
        Types typeUtils = getTypeUtils();

        TypeElement element = (TypeElement)typeUtils.asElement(type);
        if(element == null) {
            return;
        }

        // Check ignored classes and class cache,
        // check this before processing the superclass
        // otherwise we will load an ignored types parent.
        //
        Name pkgName = getElementUtils().getPackageOf(element).getQualifiedName();
        String fileName = element.getSimpleName().toString() + ReflectionGenerator.NAME;
        String qualifiedName = pkgName + "." + fileName;

        String wholeName = element.getQualifiedName().toString();
        if(ignoredClasses.contains(wholeName)) {
            logger.fine("Ignoring " + wholeName + ", reason: In the ignored list.");
            return;
        } else if(classCache.contains(qualifiedName)) {
            logger.fine("Ignoring " + wholeName + ", reason: In the already processed list.");
            return;
        }

        // Generate the super class
        // Always generate the super class first
        TypeMirror superClass = element.getSuperclass();
        if(superClass != null) {
            generate(superClass, factory);
        }

        Writer writer = null;
        try {
            JavaFileObject jfo = getFiler().createSourceFile("reflect." + pkgName + "." + fileName);
            writer = jfo.openWriter();

            ReflectionGenerator generator = factory.createReflectionGenerator(
                "nz/co/doltech/databind/apt/reflect/Reflection.vm");

            generator.mergeTemplate(writer, type);

            // Generate reflection field classes
            List<String> fieldsAdded = generateFields(type, writer, factory);

            generateEnd(fieldsAdded, writer, factory);

            // Ensure we don't process this again
            classCache.add(qualifiedName);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error while closing the print writer.", e);
                }
            }
        }
    }

    private List<String> generateFields(TypeMirror type, Writer writer, ReflectionGenerator.Factory factory) throws Exception {
        TypeElement element = (TypeElement)getTypeUtils().asElement(type);

        List<String> fieldsAdded = new ArrayList<>();
        for(VariableElement field : fieldsIn(element)) {
            if(field.getAnnotation(IgnoreInfo.class) != null) {
                // We can ignore this field since it has
                // the IgnoreInfo annotation marker.
                continue;
            }

            if(!ElementUtils.hasAtleastOneModifier(field, Modifier.FINAL, Modifier.STATIC)) {
                if(!ElementUtils.getVisibilityFromPackage(field).equals(Visibility.PACKAGE_PRIVATE)) {
                    FieldGenerator generator = factory.createFieldGenerator(
                        "nz/co/doltech/databind/apt/reflect/FieldReflection.vm");

                    generator.mergeTemplate(writer, new FieldGenerator.FieldPair(field, element));

                    fieldsAdded.add(field.getSimpleName().toString() + FieldGenerator.NAME);
                }
            }
        }
        return fieldsAdded;
    }

    private void generateEnd(List<String> fieldsAdded, Writer writer, ReflectionGenerator.Factory factory) throws Exception {
        ReflectionEndGenerator generator = factory.createReflectionEndGenerator(
            "nz/co/doltech/databind/apt/reflect/ReflectionEnd.vm");

        generator.mergeTemplate(writer, fieldsAdded);
    }

    public List<VariableElement> fieldsIn(Element element) {
        Elements elementUtils = getElementUtils();
        PackageElement pkg = elementUtils.getPackageOf(element);

        if(Emulation.isEmulated(pkg)) {
            PackageElement pkgElement = elementUtils.getPackageElement(
                Emulation.EMUL_PREFIX + pkg.getQualifiedName());

            if(pkgElement != null) {
                String emulName = pkgElement.toString() + "." + element.getSimpleName();
                InputStream is = Emulation.openEmulationStream(emulName.replace(".", "/") + ".java");

                EmulElement emulElement = Emulation.createEmulatedElement(is, emulName);
                if(emulElement != null) {
                    return ElementFilter.fieldsIn(emulElement.getEnclosedElements());
                } else {
                    logger.warning("Failed to find emulated element for "
                        + element);
                }
            } else {
                logger.warning("Failed to find emulated package for " + pkg);
            }
        }
        return ElementFilter.fieldsIn(element.getEnclosedElements());
    }

    private List<? extends TypeMirror> getReflectClasses(Reflected reflected) {
        try {
            reflected.classes();
        }
        catch(MirroredTypesException mte) {
            return mte.getTypeMirrors();
        }
        return new ArrayList<>();
    }

    private List<? extends TypeMirror> getIgnoreClasses(Reflected reflected) {
        try {
            reflected.ignored();
        }
        catch(MirroredTypesException mte) {
            return mte.getTypeMirrors();
        }
        return new ArrayList<>();
    }



    public static List<String> getIgnoredClasses() {
        return ignoredClasses;
    }
}
