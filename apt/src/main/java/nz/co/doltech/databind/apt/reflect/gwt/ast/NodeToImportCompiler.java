package nz.co.doltech.databind.apt.reflect.gwt.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import nz.co.doltech.databind.apt.reflect.gwt.EmulImportElement;
import nz.co.doltech.databind.apt.reflect.gwt.EmulType;
import nz.co.doltech.databind.apt.reflect.gwt.StringName;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class NodeToImportCompiler extends UnitCompiler<ImportDeclaration, PackageElement> {

    private final Element parent;

    public NodeToImportCompiler(CompilationUnit compileUnit, Element parent) {
        super(compileUnit);

        this.parent = parent;
    }

    @Override
    public UnitCache<PackageElement> doCompile(ImportDeclaration unit) {
        TypeMirror type = new EmulType(TypeKind.DECLARED);

        Name name = new StringName(unit.getName().toString());
        return new UnitCache<PackageElement>(
            new EmulImportElement(type, parent, name, name, NestingKind.TOP_LEVEL, unit.isStatic(), unit.isAsterisk())
        );
    }
}
