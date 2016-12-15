package nz.co.doltech.databind.apt.reflect.gwt;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EmulElement implements Element {

    private TypeMirror type;
    private ElementKind kind;
    private Element owner;
    private List<Element> members;
    private Set<Modifier> modifiers;
    private Name simpleName;

    public EmulElement() {
    }

    public EmulElement(EmulElement copy) {
        type = copy.type;
        kind = copy.kind;
        owner = copy.owner;
        members = copy.members;
        modifiers = copy.modifiers;
        simpleName = copy.simpleName;
    }

    public EmulElement(TypeMirror type,
                       ElementKind kind,
                       Element owner,
                       List<Element> members,
                       Set<Modifier> modifiers,
                       Name simpleName) {
        this.type = type;
        this.kind = kind;
        this.owner = owner;
        this.members = members;
        this.modifiers = modifiers;
        this.simpleName = simpleName;
    }

    @Override
    public TypeMirror asType() {
        return type;
    }

    @Override
    public ElementKind getKind() {
        return kind;
    }

    @Override
    public List<? extends AnnotationMirror> getAnnotationMirrors() {
        // TODO:
        return new ArrayList<>();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        // TODO:
        return null;
    }

    @Override
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
        // TODO:
        return null;
    }

    @Override
    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    @Override
    public Name getSimpleName() {
        return simpleName;
    }

    public void setEnclosingElement(Element owner) {
        this.owner = owner;
    }

    @Override
    public Element getEnclosingElement() {
        return owner;
    }

    @Override
    public List<? extends Element> getEnclosedElements() {
        return members;
    }

    public void addEnclosedElement(Element element) {
        members.add(element);
    }

    @Override
    public <R, P> R accept(ElementVisitor<R, P> v, P p) {
        // TODO:
        throw new UnsupportedOperationException();
    }
}
