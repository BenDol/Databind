package nz.co.doltech.databind.classinfo;

public class ClassInfoProvider {
    public static IClassInfo get() {
        return ClassInfoJre.get();
    }
}
