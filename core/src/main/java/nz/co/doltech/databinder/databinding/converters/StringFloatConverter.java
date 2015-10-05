package nz.co.doltech.databinder.databinding.converters;

import nz.co.doltech.databinder.databinding.AbstractConverter;

public class StringFloatConverter extends AbstractConverter<String, Float> {

    public StringFloatConverter() {
        super(String.class, Float.class);
    }

    @Override
    public Float convert(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertBack(Float value) {
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
