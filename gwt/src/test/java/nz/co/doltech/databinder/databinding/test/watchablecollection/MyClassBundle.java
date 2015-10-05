package nz.co.doltech.databinder.databinding.test.watchablecollection;

import nz.co.doltech.databinder.classinfo.gwt.ClazzBundle;
import nz.co.doltech.databinder.classinfo.gwt.ReflectedClasses;


public interface MyClassBundle extends ClazzBundle {
    @ReflectedClasses(classes = {A.class})
    void register();
}