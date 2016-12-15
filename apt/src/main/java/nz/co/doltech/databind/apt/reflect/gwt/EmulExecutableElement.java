package nz.co.doltech.databind.apt.reflect.gwt;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class EmulExecutableElement extends EmulTypeElement implements ExecutableElement {

    private final static Logger logger = Logger.getLogger(EmulExecutableElement.class.getName());

    private TypeMirror returnType;
    private List<VariableElement> params = new ArrayList<>();

    public EmulExecutableElement(EmulExecutableElement copy) {
        this(copy, copy.returnType, copy.params);
    }

    public EmulExecutableElement(EmulTypeElement copy,
                                 TypeMirror returnType,
                                 List<VariableElement> params) {
        super(copy);

        this.returnType = returnType;
        this.params = params;
    }

    public EmulExecutableElement(TypeMirror type,
                                 ElementKind kind,
                                 Element owner,
                                 List<Element> members,
                                 Set<Modifier> modifiers,
                                 Name simpleName,
                                 Name qualifiedName,
                                 NestingKind nestingKind,
                                 TypeMirror returnType) {
        super(type, kind, owner, members, modifiers, simpleName, qualifiedName, nestingKind);
        this.returnType = returnType;
    }

    @Override
    public List<? extends TypeParameterElement> getTypeParameters() {
        return null;
    }

    @Override
    public TypeMirror getReturnType() {
        return returnType;
    }

    @Override
    public List<? extends VariableElement> getParameters() {
        return params;
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }

    @Override
    public List<? extends TypeMirror> getThrownTypes() {
        return null;
    }

    @Override
    public AnnotationValue getDefaultValue() {
        return null;
    }

    @Override
    public TypeMirror getReceiverType() {
        return null;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    public void addParameter(VariableElement var) {
        if(var != null) {
            params.add(var);

            if (var instanceof EmulVariableElement) {
                ((EmulVariableElement) var).setEnclosingElement(this);
            }
        } else {
            logger.warning("Attempted to add a null var");
        }
    }
}
