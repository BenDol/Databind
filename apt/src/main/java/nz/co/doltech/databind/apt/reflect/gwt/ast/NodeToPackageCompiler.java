package nz.co.doltech.databind.apt.reflect.gwt.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import nz.co.doltech.databind.apt.reflect.gwt.EmulPackageElement;
import nz.co.doltech.databind.apt.reflect.gwt.EmulType;
import nz.co.doltech.databind.apt.reflect.gwt.StringName;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class NodeToPackageCompiler extends UnitCompiler<PackageDeclaration, PackageElement> {

    private final Element parent;

    public NodeToPackageCompiler(CompilationUnit compileUnit, Element parent) {
        super(compileUnit);

        this.parent = parent;
    }

    @Override
    public UnitCache<PackageElement> doCompile(PackageDeclaration unit) {
        TypeMirror type = new EmulType(TypeKind.DECLARED);

        Name name = new StringName(unit.getName().toString());
        return new UnitCache<PackageElement>(
            new EmulPackageElement(type, parent, name, name, NestingKind.TOP_LEVEL)
        );
    }
}
