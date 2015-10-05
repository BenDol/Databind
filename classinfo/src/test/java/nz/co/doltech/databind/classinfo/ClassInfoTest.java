package nz.co.doltech.databind.classinfo;

import junit.framework.TestCase;

public class ClassInfoTest extends TestCase {
    public void test001() {
        Clazz<?> clz = ClassInfo.findClazz(A.class);
        assertNotNull(clz);
    }

    public void test002() {
        Clazz<?> clz = ClassInfo.findClazz(A.class);
        assertEquals(clz.getAllFields().size(), 2);
    }
}

class A {
    int b;
    @SuppressWarnings("unused")
    private int a;
}