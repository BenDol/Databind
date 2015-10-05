package nz.co.doltech.databind.core.objects;

import nz.co.doltech.databind.annotation.Observable;

@Observable
public class NoDefaultConstructor {

    protected NoDefaultConstructor(String overriding) {
        // No default constructor!
    }
}
