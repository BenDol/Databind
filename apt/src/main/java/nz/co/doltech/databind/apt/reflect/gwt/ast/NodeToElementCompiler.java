package nz.co.doltech.databind.apt.reflect.gwt.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import nz.co.doltech.databind.apt.reflect.gwt.EmulTypeElement;
import nz.co.doltech.databind.apt.reflect.util.NameUtil;

import javax.lang.model.element.Element;
import java.util.List;

public class NodeToElementCompiler extends UnitCompiler<Node, Element> {

    private final Element parent;

    public NodeToElementCompiler(CompilationUnit compileUnit, Element parent) {
        super(compileUnit);

        this.parent = parent;
    }

    public UnitCache<Element> doCompile(Node unit) {
        String name = NameUtil.getNodeName(getCompileUnit(), unit);

        // Compilation Unit
        if(unit instanceof CompilationUnit) {
            EmulTypeElement baseElem = null;
            String baseName = null;

            List<TypeDeclaration> types = ((CompilationUnit) unit).getTypes();
            for(TypeDeclaration type : types) {
                Element parent = baseElem != null ? baseElem : this.parent;

                EmulTypeElement typeElement = (EmulTypeElement)new UnitCache<Element>(
                    new NodeToTypeElementCompiler(getCompileUnit(), parent).compile(type, type.getName()),
                    type.getName()
                ).getUnit();

                if(baseElem == null) {
                    baseElem = typeElement;
                    baseName = type.getName();
                } else {
                    typeElement.addEnclosedElement(typeElement);
                }
            }

            return new UnitCache<Element>(baseElem, baseName);
        }
        // Type
        else if(unit instanceof TypeDeclaration) {
            return new UnitCache<Element>(
                new NodeToTypeElementCompiler(getCompileUnit(), parent).compile((TypeDeclaration) unit, name),
                name
            );
        // Package
        } else if(unit instanceof PackageDeclaration) {
            return new UnitCache<Element>(
                new NodeToPackageCompiler(getCompileUnit(), parent).compile((PackageDeclaration)unit, name),
                name
            );
        // Import
        } else if(unit instanceof ImportDeclaration) {
            return new UnitCache<Element>(
                new NodeToImportCompiler(getCompileUnit(), parent).compile((ImportDeclaration)unit, name),
                name
            );
        // Constructor
        } else if(unit instanceof ConstructorDeclaration) {
            return new UnitCache<Element>(
                new NodeToExecutableElementCompiler(getCompileUnit(), parent).compile((ConstructorDeclaration) unit, name),
                name
            );
        // Field
        } else if(unit instanceof FieldDeclaration) {
            return new UnitCache<Element>(
                new NodeToVariableElementCompiler(getCompileUnit(), parent).compile((FieldDeclaration) unit, name),
                name
            );
        // Method
        } else if(unit instanceof MethodDeclaration) {
            return new UnitCache<Element>(
                new NodeToExecutableElementCompiler(getCompileUnit(), parent).compile((MethodDeclaration) unit, name),
                name
            );
        }
        return null;
    }

    // Class Modifiers

    // Class Name

    // Class implementations/superclass

    // Constructors

    // Fields

    // Methods
}
