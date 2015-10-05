package nz.co.doltech.databind.core.gwt;

import nz.co.doltech.databind.gwt.annotation.Observable;

import java.util.List;

@Observable
public class SampleDTO3<T> {
    List<T> totos;

    public List<T> getTotos() {
        return totos;
    }

    public void setTotos(List<T> totos) {
        this.totos = totos;
    }
}
