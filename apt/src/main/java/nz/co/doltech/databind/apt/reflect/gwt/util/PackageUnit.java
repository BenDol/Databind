package nz.co.doltech.databind.apt.reflect.gwt.util;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class PackageUnit extends VoidVisitorAdapter<Object> implements SignleUnit<PackageDeclaration> {

    private PackageDeclaration unit;

    @Override
    public void visit(PackageDeclaration dec, Object arg) {
        unit = dec;
    }

    @Override
    public PackageDeclaration getUnit() {
        return unit;
    }
}