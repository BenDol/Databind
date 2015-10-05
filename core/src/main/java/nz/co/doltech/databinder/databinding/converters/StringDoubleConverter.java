package nz.co.doltech.databinder.databinding.converters;

import nz.co.doltech.databinder.databinding.AbstractConverter;

public class StringDoubleConverter extends AbstractConverter<String, Double> {

    public StringDoubleConverter() {
        super(String.class, Double.class);
    }

    @Override
    public Double convert(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertBack(Double value) {
        try {
            return String.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
