package nz.co.doltech.databind.apt.reflect.gwt.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import nz.co.doltech.databind.apt.reflect.gwt.EmulExecutableElement;
import nz.co.doltech.databind.apt.reflect.gwt.EmulType;
import nz.co.doltech.databind.apt.reflect.gwt.StringName;
import nz.co.doltech.databind.apt.reflect.util.ModifierUtil;
import nz.co.doltech.databind.apt.reflect.util.NameUtil;
import nz.co.doltech.databind.apt.reflect.util.TypeUtil;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NodeToExecutableElementCompiler extends UnitCompiler<BodyDeclaration, ExecutableElement> {

    private final Element parent;

    public NodeToExecutableElementCompiler(CompilationUnit compileUnit, Element parent) {
        super(compileUnit);

        this.parent = parent;
    }

    @Override
    protected UnitCache<ExecutableElement> doCompile(BodyDeclaration unit) {
        if(unit instanceof ConstructorDeclaration) {
            return asConstructor((ConstructorDeclaration) unit);
        }
        else if(unit instanceof MethodDeclaration) {
            return asMethod((MethodDeclaration) unit);
        }
        return null;
    }

    private UnitCache<ExecutableElement> asConstructor(ConstructorDeclaration unit) {
        String unitName = unit.getName();

        TypeMirror type = new EmulType(TypeKind.EXECUTABLE);
        Name name = new StringName(unitName);

        ElementKind kind = ElementKind.METHOD;
        List<Element> members = new ArrayList<>();
        Set<Modifier> modifierSet = ModifierUtil.asSet(unit.getModifiers());

        EmulExecutableElement element = new EmulExecutableElement(type, kind, parent, members,
            modifierSet, name, name, NestingKind.MEMBER, null);

        for(Parameter param : unit.getParameters()) {
            NodeToVariableElementCompiler compiler = new NodeToVariableElementCompiler(getCompileUnit(), element);

            String paramName = NameUtil.getNodeName(getCompileUnit(), param);
            VariableDeclaratorId id = new VariableDeclaratorId(paramName);

            FieldDeclaration fd = new FieldDeclaration(param.getModifiers(), param.getType(),
                new VariableDeclarator(id));

            element.addParameter(compiler.compile(fd, paramName));
        }

        // Children
        NodeToElementCompiler compiler = new NodeToElementCompiler(getCompileUnit(), element);
        for(Node child : unit.getChildrenNodes()) {
            Element childElem = compiler.compile(child, NameUtil.getNodeName(getCompileUnit(), child));
            if(childElem != null) {
                members.add(childElem);
            }
        }

        return new UnitCache<ExecutableElement>(element, unitName);
    }

    private UnitCache<ExecutableElement> asMethod(MethodDeclaration unit) {
        String unitName = unit.getName();

        TypeMirror type = new EmulType(TypeKind.EXECUTABLE);
        TypeMirror returnType = TypeUtil.toMirror(getCompileUnit(), unit.getType());
        Name name = new StringName(unitName);

        ElementKind kind = ElementKind.METHOD;
        List<Element> members = new ArrayList<>();
        Set<Modifier> modifierSet = ModifierUtil.asSet(unit.getModifiers());

        EmulExecutableElement element = new EmulExecutableElement(type, kind, parent, members,
            modifierSet, name, name, NestingKind.MEMBER, returnType);

        for(Parameter param : unit.getParameters()) {
            NodeToVariableElementCompiler compiler = new NodeToVariableElementCompiler(getCompileUnit(), element);

            String paramName = NameUtil.getNodeName(getCompileUnit(), param);
            VariableDeclaratorId id = new VariableDeclaratorId(paramName);

            FieldDeclaration fd = new FieldDeclaration(param.getModifiers(), param.getType(),
                new VariableDeclarator(id));

            // Ensure the field has a parent
            fd.setParentNode(unit);

            try {
                element.addParameter(compiler.compile(fd, paramName));
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
        }

        // Children
        NodeToElementCompiler compiler = new NodeToElementCompiler(getCompileUnit(), element);
        for(Node child : unit.getChildrenNodes()) {
            Element childElem = compiler.compile(child, NameUtil.getNodeName(getCompileUnit(), child));
            if(childElem != null) {
                members.add(childElem);
            }
        }

        return new UnitCache<ExecutableElement>(element, unitName);
    }
}
