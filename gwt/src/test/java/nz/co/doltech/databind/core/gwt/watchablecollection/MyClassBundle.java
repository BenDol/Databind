package nz.co.doltech.databind.core.gwt.watchablecollection;

import nz.co.doltech.databind.classinfo.gwt.ClazzBundle;
import nz.co.doltech.databind.classinfo.gwt.ReflectedClasses;


public interface MyClassBundle extends ClazzBundle {
    @ReflectedClasses(classes = {A.class})
    void register();
}