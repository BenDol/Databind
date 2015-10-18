package nz.co.doltech.databind.apt.reflect.gwt.util;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodUnits extends VoidVisitorAdapter<Object> implements MultipleUnit<MethodDeclaration> {

    private List<MethodDeclaration> units = new ArrayList<>();

    @Override
    public void visit(MethodDeclaration dec, Object arg) {
        units.add(dec);
    }

    @Override
    public List<MethodDeclaration> getUnits() {
        return units;
    }
}