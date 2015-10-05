package nz.co.doltech.databinder.classinfo;

import nz.co.doltech.databinder.classinfo.gwt.ClassInfoGwt;

public class ClassInfoProvider {
    public static IClassInfo get() {
        return new ClassInfoGwt();
    }
}
