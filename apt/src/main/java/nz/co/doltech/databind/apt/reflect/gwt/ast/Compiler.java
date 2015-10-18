package nz.co.doltech.databind.apt.reflect.gwt.ast;

import javax.lang.model.element.Element;

public interface Compiler<I, O> {
    O compile(I unit, String unitName);
}
