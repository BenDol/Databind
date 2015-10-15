package nz.co.doltech.databind.apt;

import javax.lang.model.element.TypeElement;

public class ClassDefinition {
    private final String packageName;
    private final String className;
    private final String typeParameters;
    private final String type;

    public ClassDefinition(TypeElement type) {
        String parameterizedQualifiedName = TypeSimplifier.getTypeQualifiedName(type.asType());

        String qualifiedClassName;
        int typeParameterStart = parameterizedQualifiedName.indexOf("<");

        if (typeParameterStart != -1) {
            int typeParameterEnd = parameterizedQualifiedName.lastIndexOf(">");
            typeParameters = parameterizedQualifiedName.substring(typeParameterStart + 1, typeParameterEnd);
            qualifiedClassName = parameterizedQualifiedName.substring(0, typeParameterStart);
        } else {
            typeParameters = null;
            qualifiedClassName = parameterizedQualifiedName;
        }

        // primitives won't have packages
        int lastDot = qualifiedClassName.lastIndexOf('.');
        if (lastDot != -1) {
            packageName = qualifiedClassName.substring(0, lastDot);
            className = qualifiedClassName.substring(lastDot + 1);
        } else {
            packageName = "";
            className = qualifiedClassName;
        }

        this.type = type.asType().getKind().name();
    }

    public ClassDefinition(
            String packageName,
            String className) {
        this(packageName, className, null);
    }

    public ClassDefinition(
        String packageName,
        String className,
        String typeParameters) {
        this(packageName, className, typeParameters, null);
    }

    public ClassDefinition(
            String packageName,
            String className,
            String typeParameters,
            String type) {
        this.packageName = packageName;
        this.className = className;
        this.typeParameters = typeParameters;
        this.type = type;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getTypeParameters() {
        return typeParameters;
    }

    public String getType() {
        return type;
    }

    public boolean isParameterized() {
        return typeParameters != null;
    }

    public String getQualifiedName() {
        String qualifiedName = packageName;
        if (!qualifiedName.isEmpty()) {
            qualifiedName += ".";
        }

        qualifiedName += className;

        return qualifiedName;
    }

    public String getParameterizedClassName() {
        return maybeAppendTypeParameters(getClassName());
    }

    public String getParameterizedQualifiedName() {
        return maybeAppendTypeParameters(getQualifiedName());
    }

    @Override
    public String toString() {
        return getParameterizedQualifiedName();
    }

    @Override
    public int hashCode() {
        return getParameterizedQualifiedName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof ClassDefinition)) {
            return false;
        }

        ClassDefinition other = (ClassDefinition) obj;
        return getParameterizedQualifiedName().equals(other.getParameterizedQualifiedName());
    }

    private String maybeAppendTypeParameters(String name) {
        if (isParameterized()) {
            return name + "<" + typeParameters + ">";
        }

        return name;
    }
}