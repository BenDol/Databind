package nz.co.doltech.databinder.databinding.test.objects;

import nz.co.doltech.databinder.databinding.annotation.Observable;

@Observable
public class NoDefaultConstructor {

    protected NoDefaultConstructor(String overriding) {
        // No default constructor!
    }
}
