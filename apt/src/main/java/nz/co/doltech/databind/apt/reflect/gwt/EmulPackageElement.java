package nz.co.doltech.databind.apt.reflect.gwt;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

public class EmulPackageElement extends EmulTypeElement implements PackageElement {

    public EmulPackageElement(TypeMirror type,
                              Element owner,
                              Name simpleName,
                              Name qualifiedName,
                              NestingKind nestingKind) {
        super(type, ElementKind.PACKAGE, owner, null, null, simpleName, qualifiedName, nestingKind);
    }

    @Override
    public boolean isUnnamed() {
        return getQualifiedName() == null;
    }
}
