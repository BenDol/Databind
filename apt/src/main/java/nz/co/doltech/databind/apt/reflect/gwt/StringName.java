package nz.co.doltech.databind.apt.reflect.gwt;

import javax.lang.model.element.Name;

public class StringName implements Name {
    private final String name;

    public StringName(String name) {
        this.name = name;
    }

    @Override
    public boolean contentEquals(CharSequence cs) {
        return name.equals(cs);
    }

    @Override
    public int length() {
        return name.length();
    }

    @Override
    public char charAt(int index) {
        return name.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return name.subSequence(start, end);
    }

    @Override
    public String toString() {
        return name;
    }
}
