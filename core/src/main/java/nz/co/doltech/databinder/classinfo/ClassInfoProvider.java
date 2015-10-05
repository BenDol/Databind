package nz.co.doltech.databinder.classinfo;

public class ClassInfoProvider {
    public static IClassInfo get() {
        return ClassInfoJre.get();
    }
}
