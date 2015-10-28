package nz.co.doltech.databind.apt.reflect.gwt.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import nz.co.doltech.databind.apt.reflect.gwt.EmulVariableElement;
import nz.co.doltech.databind.apt.reflect.gwt.StringName;
import nz.co.doltech.databind.apt.reflect.util.ModifierUtil;
import nz.co.doltech.databind.apt.reflect.util.NameUtil;
import nz.co.doltech.databind.apt.reflect.util.TypeUtil;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NodeToVariableElementCompiler extends UnitCompiler<FieldDeclaration, VariableElement> {

    private final Element parent;

    public NodeToVariableElementCompiler(CompilationUnit compileUnit, Element parent) {
        super(compileUnit);

        this.parent = parent;
    }

    @Override
    protected UnitCache<VariableElement> doCompile(FieldDeclaration unit) {
        String unitName = NameUtil.getNodeName(getCompileUnit(), unit);

        TypeMirror type = TypeUtil.toMirror(getCompileUnit(), unit.getType());
        Name name = new StringName(unitName);

        ElementKind kind = ElementKind.FIELD;
        Set<Modifier> modifierSet = ModifierUtil.asSet(unit.getModifiers());

        List<Element> members = new ArrayList<>();
        for(VariableDeclarator var : unit.getVariables()) {
            // TODO:
        }

        VariableElement element = new EmulVariableElement(type, kind, parent, members,
            modifierSet, name, name, NestingKind.MEMBER);

        // Children
        NodeToElementCompiler compiler = new NodeToElementCompiler(getCompileUnit(), element);
        for(Node child : unit.getChildrenNodes()) {
            Element childElem = compiler.compile(child);
            if(childElem != null) {
                members.add(childElem);
            }
        }

        return new UnitCache<>(element);
    }
}
