package nz.co.doltech.databind.apt.reflect.gwt;

import javax.lang.model.element.Name;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;

public class EmulType implements TypeMirror {

    private TypeKind kind;
    private Name qualifiedName;

    public EmulType() {
    }

    public EmulType(TypeKind kind) {
        this.kind = kind;
    }

    public EmulType(Name qualifiedName, TypeKind kind) {
        this.qualifiedName = qualifiedName;
        this.kind = kind;
    }

    @Override
    public TypeKind getKind() {
        return kind;
    }

    public void setKind(TypeKind kind) {
        this.kind = kind;
    }

    public Name getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(Name qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    @Override
    public <R, P> R accept(TypeVisitor<R, P> v, P p) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return qualifiedName != null ? qualifiedName.toString() : "";
    }
}
