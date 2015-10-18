package nz.co.doltech.databind.apt.reflect.gwt.javaparser;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class DefaultCompilationUnitService implements CompilationUnitService {

    private JavaType name;
    private JavaPackage unitPackage;

    private List<ImportDeclaration> imports = new ArrayList<>();
    private List<TypeDeclaration> innerTypes = new ArrayList<>();

    public DefaultCompilationUnitService(JavaPackage unitPackage,
                                         JavaType name,
                                         List<ImportDeclaration> imports,
                                         List<TypeDeclaration> innerTypes) {
        this.unitPackage = unitPackage;
        this.name = name;
        this.imports = imports;
        this.innerTypes = innerTypes;
    }

    @Override
    public JavaPackage getUnitPackage() {
        return unitPackage;
    }

    @Override
    public JavaType getEnclosingTypeName() {
        return name;
    }

    @Override
    public List<ImportDeclaration> getImports() {
        return imports;
    }

    @Override
    public List<TypeDeclaration> getInnerTypes() {
        return innerTypes;
    }
};
