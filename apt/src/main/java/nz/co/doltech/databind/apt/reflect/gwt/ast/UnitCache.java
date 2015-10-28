package nz.co.doltech.databind.apt.reflect.gwt.ast;

public class UnitCache<O> {

    private O unit;

    public UnitCache(O unit) {
        this.unit = unit;
    }

    public O getUnit() {
        return unit;
    }
}
