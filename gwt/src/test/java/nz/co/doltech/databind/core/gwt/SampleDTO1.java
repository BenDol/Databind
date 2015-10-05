package nz.co.doltech.databind.core.gwt;

import java.util.HashMap;
import java.util.List;

import nz.co.doltech.databind.gwt.annotation.Observable;

@Observable
public class SampleDTO1 {
    String tata;
    int toto;
    boolean tutu;
    long l;
    float f;
    byte b;

    String[] tatas;
    int[] totos;
    boolean[] tutus;
    long[] ls;
    float fs[];
    byte bs[];

    List<Object> os;

    HashMap<String, List<SampleDTO1>> map;

    SampleDTO1 titi;
}
