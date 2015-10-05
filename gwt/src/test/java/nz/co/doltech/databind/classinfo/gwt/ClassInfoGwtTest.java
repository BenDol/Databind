package nz.co.doltech.databind.classinfo.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

import nz.co.doltech.databind.classinfo.ClassInfo;
import nz.co.doltech.databind.classinfo.Clazz;

interface TestClazzBundle extends ClazzBundle {
    @ReflectedClasses(classes = {A.class})
    void register();
}

public class ClassInfoGwtTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "nz.co.doltech.databind.classinfo.ClassInfoTest";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();

        // Registers the class introspection bundle
        GWT.<TestClazzBundle>create(TestClazzBundle.class).register();
    }

    public void testRegisteredClazz() {
        Clazz<?> clz = ClassInfo.findClazz(A.class);
        assertNotNull(clz);
    }

    public void testFields() {
        Clazz<?> clz = ClassInfo.findClazz(A.class);
        assertEquals(clz.getAllFields().size(), 2);
    }
}