package nz.co.doltech.databind.apt.reflect.gwt.ast;

public class UnitCache<O> {

    private O unit;
    private String inputName;

    public UnitCache(O unit, String inputName) {
        this.unit = unit;
        this.inputName = inputName;
    }

    public O getUnit() {
        return unit;
    }

    public String getInputName() {
        return inputName;
    }
}
