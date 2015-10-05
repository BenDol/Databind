package nz.co.doltech.databinder.databinding.test;

import nz.co.doltech.databinder.databinding.gwt.annotation.Observable;

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
