package nz.co.doltech.databind.apt.reflect.gwt.ast;

public interface Compiler<I, O> {
    O compile(I unit);
}
