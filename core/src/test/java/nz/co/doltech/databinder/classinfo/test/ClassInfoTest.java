package nz.co.doltech.databinder.classinfo.test;

import junit.framework.TestCase;
import nz.co.doltech.databinder.classinfo.ClassInfo;
import nz.co.doltech.databinder.classinfo.Clazz;

public class ClassInfoTest extends TestCase {
    public void test001() {
        Clazz<?> clz = ClassInfo.FindClazz(A.class);
        assertNotNull(clz);
    }

    public void test002() {
        Clazz<?> clz = ClassInfo.FindClazz(A.class);
        assertEquals(clz.getAllFields().size(), 2);
    }
}

class A {
    int b;
    @SuppressWarnings("unused")
    private int a;
}