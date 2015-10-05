package nz.co.doltech.databinder.databinding;

import nz.co.doltech.databinder.databinding.gwt.PlatformSpecificGwt;

public class PlatformSpecificProvider {
    public static PlatformSpecific get() {
        return PlatformSpecificGwt.get();
    }
}
