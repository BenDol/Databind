package nz.co.doltech.databind.apt.reflect.gwt;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;

public class EmulImportElement extends EmulPackageElement implements PackageElement {

    private boolean _static;
    private boolean asterisk;

    public EmulImportElement(TypeMirror type,
                             Element owner,
                             Name simpleName,
                             Name qualifiedName,
                             NestingKind nestingKind,
                             boolean _static,
                             boolean asterisk) {
        super(type, owner, simpleName, qualifiedName, nestingKind);

        this._static = _static;
        this.asterisk = asterisk;
    }

    public boolean isStatic() {
        return _static;
    }

    public boolean isAsterisk() {
        return asterisk;
    }
}
