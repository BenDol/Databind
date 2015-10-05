package nz.co.doltech.databind.core.inherited;

import nz.co.doltech.databind.annotation.Observable;
import nz.co.doltech.databind.core.inherited.foreign.Ancestor;

@Observable(inherit = true)
public class Grandparent extends Ancestor {

    protected int grandparentsProtectedNumber;
    String grandparentsString;
    int grandparentsNumber;
    private double grandparentsDouble;
}
