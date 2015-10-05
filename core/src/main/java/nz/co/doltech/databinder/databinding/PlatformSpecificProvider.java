package nz.co.doltech.databinder.databinding;

public class PlatformSpecificProvider {
    public static PlatformSpecific get() {
        return PlatformSpecificJre.get();
    }
}
