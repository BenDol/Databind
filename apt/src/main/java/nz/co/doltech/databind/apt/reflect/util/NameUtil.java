package nz.co.doltech.databind.apt.reflect.util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NamedNode;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BaseParameter;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.NameExpr;

import javax.lang.model.element.Element;

public class NameUtil {

    public static String getNodeName(CompilationUnit compileUnit, Object node) {
        if(node instanceof TypeDeclaration) {
            String nodeName = ((NamedNode) node).getName();
            if (!nodeName.contains(".")) {
                return TypeUtil.determineQualifiedName(compileUnit, new NameExpr(nodeName));
            }
            return ((NamedNode) node).getName();
        } else if(node instanceof NamedNode) {
            return ((NamedNode) node).getName();
        } else if(node instanceof ImportDeclaration) {
            return ((ImportDeclaration) node).getName().toString();
        } else if(node instanceof CompilationUnit) {
            return ((CompilationUnit) node).getPackage().getName().toString();
        } else if(node instanceof PackageDeclaration) {
            return ((PackageDeclaration) node).getName().toString();
        } else if(node instanceof BaseParameter) {
            return ((BaseParameter) node).getId().getName();
        } else if(node instanceof VariableDeclarator) {
            return ((VariableDeclarator) node).getId().getName();
        } else if(node instanceof FieldDeclaration) {
            String name = "";
            boolean useComma = false;
            for(VariableDeclarator var : ((FieldDeclaration) node).getVariables()) {
                name += var.getId().getName() + (useComma ? "," : "");
                useComma = true;
            }
            return name;
        } else if(node instanceof Element) {
            return ((Element) node).getSimpleName().toString();
        }
        return null;
    }

    /**
     * Indicates whether two {@link NameExpr} expressions are equal.
     * <p>
     * This method is necessary given {@link NameExpr} does not offer an equals
     * method.
     *
     * @param o1 the first entry to compare (null is acceptable)
     * @param o2 the second entry to compare (null is acceptable)
     * @return true if and only if both entries are identical
     */
    public static boolean isEqual(final NameExpr o1, final NameExpr o2) {
        if (o1 == null && o2 == null) {
            return true;
        }
        if (o1 == null && o2 != null) {
            return false;
        }
        if (o1 != null && o2 == null) {
            return false;
        }
        if (o1 != null && !o1.getName().equals(o2.getName())) {
            return false;
        }
        return o1 != null && o1.toString().equals(o2.toString());
    }
}
