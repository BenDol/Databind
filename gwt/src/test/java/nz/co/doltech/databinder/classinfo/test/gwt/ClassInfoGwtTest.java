package nz.co.doltech.databinder.classinfo.test.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

import nz.co.doltech.databinder.classinfo.ClassInfo;
import nz.co.doltech.databinder.classinfo.Clazz;
import nz.co.doltech.databinder.classinfo.gwt.ClazzBundle;
import nz.co.doltech.databinder.classinfo.gwt.ReflectedClasses;

interface TestClazzBundle extends ClazzBundle {
    @ReflectedClasses(classes = {A.class})
    void register();
}

public class ClassInfoGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "nz.co.doltech.databinder.classinfo.ClassInfoTest";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();

        // Registers the class introspection bundle
        GWT.<TestClazzBundle>create(TestClazzBundle.class).register();
    }

    public void testRegisteredClazz() {
        Clazz<?> clz = ClassInfo.FindClazz(A.class);
        assertNotNull(clz);
    }

    public void testFields() {
        Clazz<?> clz = ClassInfo.FindClazz(A.class);
        assertEquals(clz.getAllFields().size(), 2);
    }
}