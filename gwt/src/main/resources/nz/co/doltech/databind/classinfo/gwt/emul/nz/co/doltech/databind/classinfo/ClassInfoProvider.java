package nz.co.doltech.databind.classinfo;

import nz.co.doltech.databind.classinfo.IClassInfo;
import nz.co.doltech.databind.classinfo.gwt.ClassInfoGwt;

public class ClassInfoProvider {
    public static IClassInfo get() {
        return new ClassInfoGwt();
    }
}
