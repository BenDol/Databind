package nz.co.doltech.databind.apt.reflect.gwt;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

public class EmulVariableElement extends EmulTypeElement implements VariableElement {

    public EmulVariableElement(EmulTypeElement copy) {
        super(copy);
    }

    public EmulVariableElement(TypeMirror type,
                               ElementKind kind,
                               Element owner,
                               List<Element> members,
                               Set<Modifier> modifiers,
                               Name simpleName,
                               Name qualifiedName,
                               NestingKind nestingKind) {
        super(type, kind, owner, members, modifiers, simpleName, qualifiedName, nestingKind);
    }

    @Override
    public Object getConstantValue() {
        throw new UnsupportedOperationException();
    }
}
