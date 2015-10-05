package nz.co.doltech.databind.core.converters;

import nz.co.doltech.databind.core.AbstractConverter;

public class StringIntegerConverter extends AbstractConverter<String, Integer> {

    public StringIntegerConverter() {
        super(String.class, Integer.class);
    }

    @Override
    public Integer convert(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertBack(Integer value) {
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
