package nz.co.doltech.databind.apt.reflect.gwt.javaparser;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.List;

/**
 * An interface that enables Java Parser types to query relevant information
 * about a compilation unit.
 * 
 * @author Ben Alex
 * @author James Tyrrell
 * @since 1.0
 */
public interface CompilationUnitService {

    JavaPackage getUnitPackage();

    /**
     * @return the enclosing type (never null)
     */
    JavaType getEnclosingTypeName();

    List<ImportDeclaration> getImports();

    /**
     * @return the names of each inner type and the enclosing type (never null
     *         but may be empty)
     */
    List<TypeDeclaration> getInnerTypes();
}