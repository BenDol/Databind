package nz.co.doltech.databind.apt.reflect.gwt.util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.QualifiedNameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.PrimitiveType.*;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import nz.co.doltech.databind.apt.reflect.gwt.EmulType;
import nz.co.doltech.databind.apt.reflect.gwt.StringName;
import nz.co.doltech.databind.apt.reflect.gwt.javaparser.JdkJavaType;
import org.apache.commons.lang.Validate;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public class TypeUtil {

    public static TypeMirror toMirror(CompilationUnit compilationUnit, Type type) {
        Validate.notNull(type, "The reference type must be provided");

        EmulType emulType = new EmulType(asKind(type));

        if (type instanceof VoidType) {
            emulType.setQualifiedName(new StringName(Void.class.getName()));
            return emulType;
        }

        int array = 0;

        Type internalType = type;
        if (internalType instanceof ReferenceType) {
            array = ((ReferenceType) internalType).getArrayCount();
            if (array > 0) {
                internalType = ((ReferenceType) internalType).getType();
            }
        }

        if (internalType instanceof PrimitiveType) {
            final PrimitiveType pt = (PrimitiveType) internalType;
            if (pt.getType().equals(Primitive.Boolean)) {
                emulType.setQualifiedName(new StringName(Boolean.class.getName()));
            }
            else if (pt.getType().equals(Primitive.Char)) {
                emulType.setQualifiedName(new StringName(Character.class.getName()));
            }
            else if (pt.getType().equals(Primitive.Byte)) {
                emulType.setQualifiedName(new StringName(Byte.class.getName()));
            }
            else if (pt.getType().equals(Primitive.Short)) {
                emulType.setQualifiedName(new StringName(Short.class.getName()));
            }
            else if (pt.getType().equals(Primitive.Int)) {
                emulType.setQualifiedName(new StringName(Integer.class.getName()));
            }
            else  if (pt.getType().equals(Primitive.Long)) {
                emulType.setQualifiedName(new StringName(Long.class.getName()));
            }
            else if (pt.getType().equals(Primitive.Float)) {
                emulType.setQualifiedName(new StringName(Float.class.getName()));
            }
            else if (pt.getType().equals(Primitive.Double)) {
                emulType.setQualifiedName(new StringName(Double.class.getName()));
            } else {
                throw new IllegalStateException("Unsupported primitive '"
                    + pt.getType() + "'");
            }
        }
        else if (internalType instanceof WildcardType) {
            // We only provide very primitive support for wildcard types; Roo
            // only needs metadata at the end of the day,
            // not complete binding support from an AST
            emulType.setQualifiedName(new StringName(Object.class.getName()));
        } else {
            ClassOrInterfaceType cit = null;
            if (internalType instanceof ClassOrInterfaceType) {
                cit = (ClassOrInterfaceType) internalType;

                // are we a generic type
                if(type instanceof ReferenceType) {
                    cit.setName(Object.class.getName());
                }
            } else if (internalType instanceof ReferenceType) {
                cit = (ClassOrInterfaceType) ((ReferenceType) type).getType();

                // Hack to simply provide Object with generics
                if(!cit.getName().contains("<")) {
                    cit.setName(Object.class.getName());
                }
            }

            String qualifiedName = internalType.toString();
            if(cit != null) {
                NameExpr nameExpr = new NameExpr(cit.getName());
                if(!nameExpr.getName().contains(".")) {
                    qualifiedName = determineQualifiedName(compilationUnit, nameExpr);
                }
            }

            for(int i = 0; i < array; i++) {
                qualifiedName += "[]";
            }
            emulType.setQualifiedName(new StringName(qualifiedName));
        }

        return emulType;
    }

    public static TypeKind asKind(Type type) {
        if(type instanceof ClassOrInterfaceType) {
            return TypeKind.DECLARED;
        } else if(type instanceof PrimitiveType) {
            switch (((PrimitiveType) type).getType()) {
                case Boolean:
                    return TypeKind.BOOLEAN;
                case Byte:
                    return TypeKind.BYTE;
                case Char:
                    return TypeKind.CHAR;
                case Double:
                    return TypeKind.DOUBLE;
                case Float:
                    return TypeKind.FLOAT;
                case Int:
                    return TypeKind.INT;
                case Long:
                    return TypeKind.LONG;
                case Short:
                    return TypeKind.SHORT;
            }

        } else if(type instanceof ReferenceType) {
            return asKind(((ReferenceType) type).getType());
        } else if(type instanceof UnknownType) {
            return TypeKind.OTHER;
        } else if(type instanceof VoidType) {
            return TypeKind.VOID;
        } else if(type instanceof WildcardType) {
            return TypeKind.WILDCARD;
        }
        return TypeKind.ERROR;
    }

    /**
     * Looks up the import declaration applicable to the presented name
     * expression.
     * <p>
     * If a fully-qualified name is passed to this method, the corresponding
     * import will be evaluated for a complete match. If a simple name is passed
     * to this method, the corresponding import will be evaluated if its simple
     * name matches. This therefore reflects the normal Java semantics for using
     * simple type names that have been imported.
     *
     * @param compilationUnit the types in the compilation unit
     *            (required)
     * @param nameExpr the expression to locate an import for (which would
     *            generally be a {@link NameExpr} and thus not have a package
     *            identifier; required)
     * @return the relevant import, or null if there is no import for the
     *         expression
     */
    public static ImportDeclaration getImportDeclarationFor(
        final CompilationUnit compilationUnit,
        final NameExpr nameExpr) {
        Validate.notNull(compilationUnit,
            "Compilation unit services required");
        Validate.notNull(nameExpr, "Name expression required");

        final List<ImportDeclaration> imports = compilationUnit
            .getImports();

        for (final ImportDeclaration candidate : imports) {
            final NameExpr candidateNameExpr = candidate.getName();
            if (!candidate.toString().contains("*")) {
                if(!(candidateNameExpr instanceof QualifiedNameExpr)) {
                    System.out.println("Expected import '" + candidate
                        + "' to use a fully-qualified type name");
                }
            }
            if (nameExpr instanceof QualifiedNameExpr) {
                // User is asking for a fully-qualified name; let's see if there
                // is a full match
                if (NameUtil.isEqual(nameExpr, candidateNameExpr)) {
                    return candidate;
                }
            }
            else {
                // User is not asking for a fully-qualified name, so let's do a
                // simple name comparison that discards the import's
                // qualified-name package
                if (candidateNameExpr.getName().equals(nameExpr.getName())) {
                    return candidate;
                }
            }
        }
        return null;
    }

    public static String determineQualifiedName(final CompilationUnit compilationUnit,
                                                final TypeDeclaration typeDeclaration) {
        NameExpr nameExpr = typeDeclaration.getNameExpr();
        if (!nameExpr.getName().contains(".")) {
            return determineQualifiedName(compilationUnit, nameExpr);
        }
        return "";
    }

    public static String determineQualifiedName(final CompilationUnit compilationUnit,
                                               final NameExpr nameExpr) {
        final ImportDeclaration importDeclaration = getImportDeclarationFor(
            compilationUnit, nameExpr);

        if (importDeclaration == null) {
            if (JdkJavaType.isPartOfJavaLang(nameExpr.getName())) {
                return "java.lang." + nameExpr.getName();
            }

            String unitPackage = compilationUnit.getPackage().getName().toString();
            return unitPackage.equals("") ? nameExpr.getName()
                : unitPackage + "." + nameExpr.getName();
        }
        return null;
    }
}
