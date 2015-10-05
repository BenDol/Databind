package nz.co.doltech.databind.core.inherited;

import nz.co.doltech.databind.annotation.Observable;

@Observable(inheritDepth = 3)
public class Child extends Parent {

    String childsString;

    private double childsPrivateNoGetterSetter;
    private double childsPrivateHasGetterSetter;

    public double getChildsPrivateHasGetterSetter() {
        return childsPrivateHasGetterSetter;
    }

    public void setChildsPrivateHasGetterSetter(double childsPrivateHasGetterSetter) {
        this.childsPrivateHasGetterSetter = childsPrivateHasGetterSetter;
    }
}
