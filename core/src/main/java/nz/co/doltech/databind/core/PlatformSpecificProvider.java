package nz.co.doltech.databind.core;

public class PlatformSpecificProvider {
    public static PlatformSpecific get() {
        return PlatformSpecificJre.get();
    }
}
