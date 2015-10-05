package nz.co.doltech.databinder.databinding.test.inherited;

import nz.co.doltech.databinder.databinding.annotation.Observable;
import nz.co.doltech.databinder.databinding.test.inherited.foreign.Ancestor;

@Observable(inherit = true)
public class Grandparent extends Ancestor {

    protected int grandparentsProtectedNumber;
    String grandparentsString;
    int grandparentsNumber;
    private double grandparentsDouble;
}
