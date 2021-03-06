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
package nz.co.doltech.databind.apt.observable;

import nz.co.doltech.databind.apt.TypeSimplifier;
import nz.co.doltech.databind.util.Observable;
import nz.co.doltech.databind.util.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes({"nz.co.doltech.databind.util.Observable"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ObservableAnnotationProcessor extends BaseAnnotationProcessor {

    protected static final int BEGIN_INDEX = 0;
    protected static final int CONSTRUCTOR_INDEX = 1;
    protected static final int COPY_CONSTRUCTOR_INDEX = 2;
    protected static final int FIELD_GETTER_INDEX = 3;
    protected static final int METHOD_SETTER_INDEX = 4;
    protected static final int FIELD_SETTER_INDEX = 5;
    private static final String TEMPLATE_CLASS = "nz/co/doltech/databind/apt/observable/TemplateClass.txt";

    static String[] getterPrefixes = new String[]{
        "get", "is"
    };
    static String[] setterPrefixes = new String[]{
        "set"
    };

    @Override
    protected String getTargetTypeName(TypeElement typeElement) {
        String sourceTypeName = typeElement.getSimpleName().toString();
        String targetTypeName;
        String SUFFIX = "Internal";
        if (sourceTypeName.endsWith(SUFFIX))
            targetTypeName = StringUtils.capitalizeFirstLetter(sourceTypeName)
                .substring(0, sourceTypeName.length() - SUFFIX.length());
        else
            targetTypeName = "Observable" + StringUtils.capitalizeFirstLetter(sourceTypeName);

        return targetTypeName;
    }

    protected int getInheritDepth(Annotation annotation) {
        if (annotation instanceof Observable) {
            Observable observable = (Observable) annotation;
            int depth = ((Observable) annotation).inheritDepth();
            return observable.inherit() ? depth : (depth != Observable.INHERIT_MAX ? depth : 0);
        }
        return -1;
    }

    protected boolean canUseCopyConstructor(Annotation annotation) {
        return annotation instanceof Observable && ((Observable) annotation).copyConstructor();
    }

    @Override
    protected void doProcess(ProcInfo procInfo, Writer writer) throws IOException {
        int inheritDepth = getInheritDepth(procInfo.annotation);

        Template template = writeClassIntro(procInfo);
        template.replace(Tags.CONSTRUCTORS, generateConstructors(procInfo, inheritDepth));
        template.replace(Tags.FIELDS_AND_METHODS, generateFieldsAndMethods(procInfo, inheritDepth));
        writer.write(template.toString());
    }

    protected Template writeClassIntro(ProcInfo procInfo) {
        Template template = Template.fromResource(TEMPLATE_CLASS, BEGIN_INDEX);

        template.replace(Tags.EXTRA_IMPORTS, generateExtraImports(procInfo));
        template.replace(Tags.CLASS_ENTRY, generateClassEntry(procInfo).toString());

        parseGeneralTags(template, procInfo);
        return template;
    }

    protected String generateExtraImports(ProcInfo procInfo) {
        return "";
    }

    protected StringBuilder generateClassEntry(ProcInfo procInfo) {
        return new StringBuilder();
    }

    private String generateConstructors(ProcInfo procInfo, int inheritDepth) {
        StringBuilder sb = new StringBuilder();

        boolean hasDefault = false;
        for (ExecutableElement constr : ElementFilter.constructorsIn(procInfo.typeElement.getEnclosedElements())) {
            if (constr.getModifiers().contains(Modifier.PRIVATE)) {
                continue;
            }
            Template ctr = Template.fromResource(TEMPLATE_CLASS, CONSTRUCTOR_INDEX);
            ctr.replace(Tags.TARGET_CLASS_NAME, procInfo.implName);

            // Verify default constructor
            if (!hasDefault) {
                hasDefault = constr.getParameters().isEmpty();
            }

            // Build the constructors parameters
            StringBuilder formalParameters = new StringBuilder();
            boolean addComa = false;
            for (VariableElement parameter : constr.getParameters()) {
                String paramTypeName = TypeSimplifier.getTypeQualifiedName(parameter.asType());

                if (addComa)
                    formalParameters.append(", ");
                else
                    addComa = true;

                formalParameters.append(paramTypeName);
                formalParameters.append(" ");
                formalParameters.append(parameter.getSimpleName());
            }
            ctr.replace(Tags.FORMAL_PARAMETERS, formalParameters.toString());

            StringBuilder parameters = new StringBuilder();
            addComa = false;
            for (VariableElement parameter : constr.getParameters()) {
                if (addComa)
                    parameters.append(", ");
                else
                    addComa = true;
                parameters.append(parameter.getSimpleName());
            }
            ctr.replace(Tags.PARAMETERS, parameters.toString());
            sb.append(ctr.toString());
        }

        // Only create the copy constructor when there is
        // a default constructor present
        if (hasDefault && canUseCopyConstructor(procInfo.annotation)) {
            Template copyCtr = Template.fromResource(TEMPLATE_CLASS, COPY_CONSTRUCTOR_INDEX);
            copyCtr.replace(Tags.TARGET_CLASS_NAME, procInfo.implName);
            copyCtr.replace(Tags.SOURCE_CLASS_NAME, procInfo.typeElement.getSimpleName().toString());
            copyCtr.replace(Tags.MAP_FIELDS, generateCopyConstructor(procInfo, procInfo.typeElement, inheritDepth));

            sb.append(copyCtr.toString());
        } else {
            sb.append("\n\t// Cannot generate copy constructor when there is no default constructor");
            sb.append("\n\t// Either add a default constructor or manually add this constructor.\n");
        }
        return sb.toString();
    }

    private String generateCopyConstructor(ProcInfo procInfo, TypeElement typeElement, int inheritDepth) {
        StringBuilder sb = new StringBuilder();

        boolean indent = false;
        for (VariableElement field : ElementFilter.fieldsIn(typeElement.getEnclosedElements())) {
            Set<Modifier> mods = field.getModifiers();
            if (mods.contains(Modifier.STATIC) || mods.contains(Modifier.FINAL)) {
                // We should be ignoring static/final fields since they
                // shouldn't be modified by a local object call.
                continue;
            }
            String fieldName = field.getSimpleName().toString();

            if (!isFieldAccessible(field, procInfo)) {
                // Ensure the method exists since this field is inaccessible
                ExecutableElement getter = getFieldGetterMethod(typeElement, fieldName);
                if (getter != null) {
                    // Generate the setter method using the getter to copy
                    // the field data from source to the target class
                    ExecutableElement setter = getFieldSetterMethod(typeElement, fieldName);
                    if (setter != null) {
                        if (indent) {
                            sb.append("\n\t\t");
                        } else {
                            indent = true;
                        }

                        sb.append(setter.getSimpleName())
                            .append("(").append("o.").append(getter.getSimpleName())
                            .append("()").append(")").append(";");
                    }
                } else {
                    msg.printMessage(Kind.WARNING, "Field cannot be accessed " +
                        "and has no getter method: " + fieldName, field);
                }
            } else {
                // Simply make the copy directly via field access
                if (indent) {
                    sb.append("\n\t\t");
                } else {
                    indent = true;
                }

                sb.append("this.").append(field.getSimpleName()).append(" = ").append("o.")
                    .append(field.getSimpleName()).append(";");
            }
        }

        if (inheritDepth > 0) {
            // Process the superclass
            TypeMirror superMirror = typeElement.getSuperclass();

            if (superMirror instanceof DeclaredType) {
                TypeElement superType = (TypeElement) ((DeclaredType) superMirror).asElement();
                // Don't process base java.lang.Object types
                if (!superType.getQualifiedName().toString().equals("java.lang.Object")) {
                    String copyCtr = generateCopyConstructor(procInfo, superType, inheritDepth - 1);
                    if (!copyCtr.isEmpty()) {
                        // Only append if there was a result
                        sb.append("\n\t\t").append(copyCtr);
                    }
                }
            }
        }
        return sb.toString();
    }

    private String generateFieldsAndMethods(ProcInfo procInfo, int inheritDepth) {
        Set<String> settersDone = new HashSet<>();
        Set<String> gettersDone = new HashSet<>();

        String methodsFromMethods = generateMethodFromMethods(procInfo.typeElement,
            settersDone, gettersDone, inheritDepth);

        String methodsFromFields = generateMethodFromFields(procInfo, procInfo.typeElement,
            settersDone, gettersDone, inheritDepth);

        return methodsFromFields + methodsFromMethods;
    }

    private String generateMethodFromFields(ProcInfo procInfo, TypeElement typeElement, Set<String> settersDone,
                                            Set<String> gettersDone, int inheritDepth) {
        StringBuilder sb = new StringBuilder();

        for (VariableElement field : ElementFilter.fieldsIn(typeElement.getEnclosedElements())) {
            if (!isFieldAccessible(field, procInfo)) {
                continue;
            }

            Set<Modifier> mods = field.getModifiers();
            if (mods.contains(Modifier.STATIC) || mods.contains(Modifier.FINAL)) {
                // We should be ignoring static/final fields since they
                // shouldn't be modified by a local object call.
                continue;
            }

            String fieldName = field.getSimpleName().toString();
            TypeMirror fieldType = field.asType();

            // Process setting field generation
            if (!settersDone.contains(fieldName)) {
                String methodName = "set" + StringUtils.capitalizeFirstLetter(fieldName);
                try {
                    sb.append(generateFieldSetterStub(methodName, fieldName, fieldType));
                } catch (IllegalStateException ex) {
                    throw new IllegalStateException(typeElement.getQualifiedName()
                        + " setter stub generation failed.", ex);
                }
                settersDone.add(fieldName);
            }

            // Process getter field generation
            if (!gettersDone.contains(fieldName)) {
                String methodName;
                if (fieldType.getKind().equals(TypeKind.BOOLEAN)) {
                    methodName = "is";
                } else {
                    methodName = "get";
                }
                methodName += StringUtils.capitalizeFirstLetter(fieldName);
                try {
                    sb.append(generateFieldGetterStub(methodName, fieldName, fieldType));
                } catch (IllegalStateException ex) {
                    throw new IllegalStateException(typeElement.getQualifiedName()
                        + " getter stub generation failed.", ex);
                }
                gettersDone.add(fieldName);
            }
        }

        if (inheritDepth > 0) {
            // Process the superclass
            TypeMirror superMirror = typeElement.getSuperclass();

            if (superMirror instanceof DeclaredType) {
                TypeElement superType = (TypeElement) ((DeclaredType) superMirror).asElement();
                // Don't process base java.lang.Object types
                if (!superType.getQualifiedName().toString().equals("java.lang.Object")) {
                    sb.append(generateMethodFromFields(procInfo, superType, settersDone, gettersDone,
                        inheritDepth - 1));
                }
            }
        }
        return sb.toString();
    }

    private String generateMethodFromMethods(TypeElement typeElement, Set<String> settersDone,
                                             Set<String> gettersDone, int inheritDepth) {
        StringBuilder sb = new StringBuilder();

        for (ExecutableElement method : ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
            String methodName = method.getSimpleName().toString();

            Set<Modifier> mods = method.getModifiers();
            if (mods.contains(Modifier.PRIVATE)) {
                continue;
            }

            String fieldName = null;
            for (String prefix : setterPrefixes) {
                if (methodName.startsWith(prefix) && Character.isUpperCase(methodName.charAt(prefix.length()))) {
                    fieldName = StringUtils.lowerFirstLetter(methodName.substring(prefix.length()));
                }
            }

            if (fieldName != null) {
                if (settersDone.contains(fieldName)) {
                    // We have already processed this field method.
                    continue;
                } else if (mods.contains(Modifier.FINAL)) {
                    msg.printMessage(Kind.ERROR, "Setter for '" + fieldName + "' field is marked " +
                        "as 'final' cannot override this method.", method);
                    continue;
                } else if (mods.contains(Modifier.ABSTRACT)) {
                    // Gracefully ignore abstract setter methods definitions
                    // We have probably already found this method anyway.
                    continue;
                }

                sb.append(generateMethodSetterStub(method, fieldName));
                settersDone.add(fieldName);
            } else {
                for (String prefix : getterPrefixes) {
                    if (methodName.startsWith(prefix) && Character.isUpperCase(methodName.charAt(prefix.length()))) {
                        fieldName = StringUtils.lowerFirstLetter(methodName.substring(prefix.length()));
                    }
                }
                if (fieldName != null) {
                    gettersDone.add(fieldName);
                }
            }
        }

        if (inheritDepth > 0) {
            // Process the superclass
            TypeMirror superMirror = typeElement.getSuperclass();

            if (superMirror instanceof DeclaredType) {
                TypeElement superType = (TypeElement) ((DeclaredType) superMirror).asElement();
                // Don't process base java.lang.Object types
                if (!superType.getQualifiedName().toString().equals("java.lang.Object")) {
                    sb.append(generateMethodFromMethods(superType, settersDone, gettersDone,
                        inheritDepth - 1));
                }
            }
        }
        return sb.toString();
    }

    private String generateFieldSetterStub(String methodName, String fieldName, TypeMirror fieldType) {
        Template setter = Template.fromResource(TEMPLATE_CLASS, FIELD_SETTER_INDEX);
        setter.replace(Tags.MODIFIERS, "public");
        setter.replace(Tags.METHOD_NAME, methodName);
        try {
            setter.replace(Tags.PROPERTY_CLASS, TypeSimplifier.getTypeQualifiedName(fieldType));
        } catch (IllegalStateException ex) {
            throw new IllegalStateException("Unable to generate field setter stub for '" +
                fieldName + "' (" + methodName + ")", ex);
        }
        setter.replace(Tags.PROPERTY, fieldName);

        return setter.toString();
    }

    private String generateFieldGetterStub(String methodName, String fieldName, TypeMirror fieldType) {
        Template getter = Template.fromResource(TEMPLATE_CLASS, FIELD_GETTER_INDEX);
        try {
            getter.replace(Tags.PROPERTY_CLASS, TypeSimplifier.getTypeQualifiedName(fieldType));
        } catch (IllegalStateException ex) {
            throw new IllegalStateException("Unable to generate field getter stub for '" +
                fieldName + "' (" + methodName + ")", ex);
        }
        getter.replace(Tags.METHOD_NAME, methodName);
        getter.replace(Tags.PROPERTY, fieldName);

        return getter.toString();
    }

    private String generateMethodSetterStub(ExecutableElement method, String fieldName) {
        Template setter = Template.fromResource(TEMPLATE_CLASS, METHOD_SETTER_INDEX);

        StringBuilder modifiersBuilder = new StringBuilder();
        for (Modifier mod : method.getModifiers()) {
            modifiersBuilder.append(mod.toString());
        }
        setter.replace(Tags.MODIFIERS, modifiersBuilder.toString());
        setter.replace(Tags.METHOD_NAME, method.getSimpleName().toString());
        setter.replace(Tags.PROPERTY_CLASS, TypeSimplifier.getTypeQualifiedName(method.getParameters().get(0).asType()));
        setter.replace(Tags.PROPERTY, fieldName);
        return setter.toString();
    }

    private ExecutableElement getFieldSetterMethod(TypeElement typeElement, String fieldName) {
        return getMethodForField(typeElement, fieldName, setterPrefixes);
    }

    private ExecutableElement getFieldGetterMethod(TypeElement typeElement, String fieldName) {
        return getMethodForField(typeElement, fieldName, getterPrefixes);
    }

    private ExecutableElement getMethodForField(TypeElement typeElement, String fieldName, String... startsWith) {
        for (ExecutableElement method : ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
            if (method.getModifiers().contains(Modifier.PRIVATE))
                continue;

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

    private boolean isFieldAccessible(VariableElement field, ProcInfo procInfo) {
        Element enclosingType = field.getEnclosingElement();

        // Check for matching package to ensure type
        // protected accessibility must be package enclosed
        String typePkg = elementUtils.getPackageOf(enclosingType).getQualifiedName().toString();
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