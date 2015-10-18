package nz.co.doltech.databind.apt.reflect.gwt.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import nz.co.doltech.databind.apt.reflect.gwt.EmulTypeElement;
import nz.co.doltech.databind.apt.reflect.gwt.EmulType;
import nz.co.doltech.databind.apt.reflect.gwt.StringName;
import nz.co.doltech.databind.apt.reflect.gwt.util.ModifierUtil;
import nz.co.doltech.databind.apt.reflect.gwt.util.NameUtil;
import nz.co.doltech.databind.apt.reflect.gwt.util.TypeUtil;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NodeToTypeElementCompiler extends UnitCompiler<TypeDeclaration, EmulTypeElement> {

    private final Element parent;

    public NodeToTypeElementCompiler(CompilationUnit compileUnit, Element parent) {
        super(compileUnit);

        this.parent = parent;
    }

    @Override
    protected UnitCache<EmulTypeElement> doCompile(TypeDeclaration unit) {
        String unitName = unit.getName();

        String qualifiedName = TypeUtil.determineQualifiedName(
            getCompileUnit(), unit.getNameExpr());

        TypeMirror type = new EmulType(new StringName(qualifiedName), TypeKind.DECLARED);
        Name name = new StringName(unitName);


        ElementKind kind = ElementKind.OTHER;
        List<Element> members = new ArrayList<>();
        Set<Modifier> modifierSet = ModifierUtil.asSet(unit.getModifiers());

        if(unit instanceof AnnotationDeclaration) {
            kind = ElementKind.ANNOTATION_TYPE;
        } else if(unit instanceof EnumDeclaration) {
            kind = ElementKind.ENUM;
        } else if(unit instanceof ClassOrInterfaceDeclaration) {
            if(((ClassOrInterfaceDeclaration) unit).isInterface()) {
                kind = ElementKind.INTERFACE;
            } else {
                kind = ElementKind.CLASS;
            }
        }

        EmulTypeElement element = new EmulTypeElement(type, kind, parent, members,
            modifierSet, name, name, NestingKind.TOP_LEVEL);

        // Children
        NodeToElementCompiler compiler = new NodeToElementCompiler(getCompileUnit(), element);
        for(BodyDeclaration member : unit.getMembers()) {
            Element childElem = compiler.compile(member, NameUtil.getNodeName(
                getCompileUnit(), member));
            if(childElem != null) {
                members.add(childElem);
            }
        }

        return new UnitCache<>(element, unitName);
    }
}
