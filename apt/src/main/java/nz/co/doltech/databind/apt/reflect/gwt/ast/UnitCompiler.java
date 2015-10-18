package nz.co.doltech.databind.apt.reflect.gwt.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import nz.co.doltech.databind.apt.reflect.util.TypeUtil;

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
    public O compile(I unit, String unitName) {
        unitName = processIdentifierName(unit, unitName);
        O output = unitName != null ? (O)compiledCache.get(unitName) : null;
        if(output != null) {
            return output;
        } else {
            UnitCache<O> unitCache = doCompile(unit);
            if(unitCache != null) {
                output = unitCache.getUnit();
                compiledCache.put(unitCache.getInputName(), output);
            }
        }
        return output;
    }

    protected abstract UnitCache<O> doCompile(I unit);

    public CompilationUnit getCompileUnit() {
        return compileUnit;
    }

    private String processIdentifierName(Object unit, String unitName) {
        if(unit != null && unitName != null) {
            if(unit instanceof TypeDeclaration) {
                unitName = TypeUtil.determineQualifiedName(compileUnit, (TypeDeclaration) unit);
            } else if(unit instanceof ConstructorDeclaration) {
                unitName = processIdentifierName(((Node) unit).getParentNode(), "") + "#ctor";
            } else if(unit instanceof MethodDeclaration) {
                unitName = processIdentifierName(((Node) unit).getParentNode(), "") + "#" + unitName;
            } else if(unit instanceof FieldDeclaration) {
                unitName = processIdentifierName(((Node) unit).getParentNode(), "") + "&" + unitName;
            }
        }
        return unitName;
    }
}
