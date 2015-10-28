package nz.co.doltech.databind.apt.reflect.gwt;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import nz.co.doltech.databind.apt.reflect.gwt.ast.NodeToElementCompiler;

import javax.lang.model.element.QualifiedNameable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Emulation {

    private static final Logger logger = Logger.getLogger(Emulation.class.getName());

    public static final String EMUL_PREFIX = "com.google.gwt.emul.";

    public static final List<String> emuls = new ArrayList<>();
    static {
        emuls.add("java.beans");
        emuls.add("java.io");
        emuls.add("java.lang");
        emuls.add("java.math");
        emuls.add("java.security");
        emuls.add("java.sql");
        emuls.add("java.text");
        emuls.add("java.util");
    }

    public static InputStream openEmulationStream(String fileName) {
        return Emulation.class.getClassLoader().getResourceAsStream(fileName);
    }

    public static boolean isEmulated(QualifiedNameable nameable) {
        String name = nameable.getQualifiedName().toString();
        for(String emul : emuls) {
            if (name.startsWith(emul)) {
                return true;
            }
        }
        return false;
    }

    public static EmulElement createEmulatedElement(InputStream in) {
        CompilationUnit cu;
        try {
            cu = JavaParser.parse(in);
            NodeToElementCompiler compiler = new NodeToElementCompiler(cu, null);
            return (EmulElement) compiler.compile(cu);
        } catch (ParseException ex) {
            ex.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Exception while closing input stream", ex);
            }
        }
        return null;
    }
}
