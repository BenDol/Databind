package nz.co.doltech.databind.apt.reflect.gwt;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

public class EmulTypeElement extends EmulElement implements TypeElement {

    private Name qualifiedName;
    private NestingKind nestingKind;

    public EmulTypeElement(EmulTypeElement copy) {
        this(copy, copy.qualifiedName, copy.nestingKind);
    }

    public EmulTypeElement(EmulElement copy,
                           Name qualifiedName,
                           NestingKind nestingKind) {
        super(copy);

        this.qualifiedName = qualifiedName;
        this.nestingKind = nestingKind;
    }

    public EmulTypeElement(TypeMirror type,
                           ElementKind kind,
                           Element owner,
                           List<Element> members,
                           Set<Modifier> modifiers,
                           Name simpleName,
                           Name qualifiedName,
                           NestingKind nestingKind) {
        super(type, kind, owner, members, modifiers, simpleName);
        this.qualifiedName = qualifiedName;
        this.nestingKind = nestingKind;
    }

    @Override
    public NestingKind getNestingKind() {
        return nestingKind;
    }

    @Override
    public Name getQualifiedName() {
        return qualifiedName;
    }

    @Override
    public TypeMirror getSuperclass() {
        return null;
    }

    @Override
    public List<? extends TypeMirror> getInterfaces() {
        return null;
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
        return null;
    }
}
