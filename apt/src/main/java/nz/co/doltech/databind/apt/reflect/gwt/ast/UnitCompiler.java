package nz.co.doltech.databind.apt.reflect.gwt.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ReferenceType;
import nz.co.doltech.databind.apt.reflect.gwt.EmulType;
import nz.co.doltech.databind.apt.reflect.util.NameUtil;
import nz.co.doltech.databind.apt.reflect.util.TypeUtil;

import javax.lang.model.element.TypeElement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public abstract class UnitCompiler<I, O> implements Compiler<I, O> {

    private static Map<String, Object> compiledCache = new ConcurrentHashMap<>();

    private CompilationUnit compileUnit;

    public UnitCompiler(CompilationUnit compileUnit) {
        this.compileUnit = compileUnit;
    }

    @Override
    public O compile(I unit) {
        String unitName = processIdentifierName(
            unit, NameUtil.getNodeName(compileUnit, unit));

        O output = unitName != null ? (O)compiledCache.get(unitName) : null;
        if(output != null) {
            return output;
        } else {
            UnitCache<O> unitCache = doCompile(unit);
            if(unitCache != null) {
                output = unitCache.getUnit();

                if(unit instanceof CompilationUnit) {
                    TypeDeclaration typeDec = ((CompilationUnit) unit).getTypes().get(0);
                    unitName = processIdentifierName(typeDec, "");
                } else {
                    unitName = processIdentifierName(unit, NameUtil.getNodeName(compileUnit, unit));
                }

                try {
                    compiledCache.put(unitName, output);
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return output;
    }

    protected abstract UnitCache<O> doCompile(I unit);

    public CompilationUnit getCompileUnit() {
        return compileUnit;
    }

    protected String processIdentifierName(Object unit, String unitName) {
        if(unit != null && unitName != null) {
            if(unit instanceof TypeDeclaration) {
                unitName = TypeUtil.determineQualifiedName(compileUnit, (TypeDeclaration) unit);
            } else if(unit instanceof ConstructorDeclaration) {
                if(!unitName.contains("#")) {
                    unitName = processIdentifierName(((Node) unit).getParentNode(), "") + "#ctor";
                }
            } else if(unit instanceof MethodDeclaration) {
                if(!unitName.contains("#")) {
                    if(unitName.isEmpty()) {
                        unitName = ((MethodDeclaration) unit).getName();
                    }
                    unitName = processIdentifierName(((Node) unit).getParentNode(), "") + "#" + unitName;
                }
            } else if(unit instanceof FieldDeclaration) {
                if(!unitName.contains("&")) {
                    if(unitName.isEmpty()) {
                        unitName = unit.toString();
                    }
                    unitName = processIdentifierName(((Node) unit).getParentNode(), "") + "&" + unitName;
                }
            } else if(unit instanceof Parameter) {
                EmulType type = (EmulType)TypeUtil.toMirror(compileUnit, ((Parameter) unit).getType());
                if(type != null) {
                    unitName = processIdentifierName(((Node) unit).getParentNode(), "")
                        + "&" + type.getQualifiedName().toString();
                }
            }
        }
        return unitName;
    }
}
