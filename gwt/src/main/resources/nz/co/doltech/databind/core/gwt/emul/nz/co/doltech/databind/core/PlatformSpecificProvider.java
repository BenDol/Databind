package nz.co.doltech.databind.core;

import nz.co.doltech.databind.core.PlatformSpecific;
import nz.co.doltech.databind.core.gwt.PlatformSpecificGwt;

public class PlatformSpecificProvider {
    public static PlatformSpecific get() {
        return PlatformSpecificGwt.get();
    }
}
