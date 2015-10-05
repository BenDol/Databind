package nz.co.doltech.databinder.databinding.test.inherited;

import nz.co.doltech.databinder.databinding.annotation.Observable;

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
