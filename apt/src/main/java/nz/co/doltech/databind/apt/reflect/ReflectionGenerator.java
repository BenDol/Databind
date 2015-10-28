package nz.co.doltech.databind.apt.reflect;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import nz.co.doltech.databind.apt.ElementUtils;
import nz.co.doltech.databind.apt.reflect.gwt.Emulation;
import nz.co.doltech.databind.apt.velocity.AbstractVelocityGenerator;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import javax.inject.Provider;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.logging.Logger;

public class ReflectionGenerator extends AbstractVelocityGenerator<TypeMirror> {

    private final static Logger logger = Logger.getLogger(ReflectionGenerator.class.getName());

    public static final String NAME = "_Reflection";

    public interface Factory {
        ReflectionGenerator createReflectionGenerator(
            @Assisted("velocityTemplate") String velocityTemplate);

        FieldGenerator createFieldGenerator(
            @Assisted("velocityTemplate") String velocityTemplate);

        ReflectionEndGenerator createReflectionEndGenerator(
            @Assisted("velocityTemplate") String velocityTemplate);

        ReflectionRegistryGenerator createReflectionRegistryGenerator(
            @Assisted("velocityTemplate") String velocityTemplate);
    }

    private final Types typeUtils;
    private final Elements elementUtils;
    private final String velocityTemplate;

    @Inject
    public ReflectionGenerator(Provider<VelocityContext> velocityContextProvider,
                               VelocityEngine velocityEngine,
                               Types typeUtils,
                               Elements elementUtils,
                               @Assisted("velocityTemplate") String velocityTemplate) {
        super(logger, velocityEngine, velocityContextProvider);

        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.velocityTemplate = velocityTemplate;
    }

    @Override
    protected String getTemplate() {
        return velocityTemplate;
    }

    @Override
    protected void populateVelocityContext(VelocityContext velocityContext, TypeMirror reflect) throws VelocityException {
        TypeElement element = (TypeElement)typeUtils.asElement(reflect);

        // Package name
        PackageElement pkgElem = elementUtils.getPackageOf(element);
        if(Emulation.isEmulated(pkgElem)) {
            String pkgName = stripEmulationPrefix(pkgElem.getQualifiedName().toString());
            velocityContext.put(PACKAGE, pkgName + ";");
        } else {
            velocityContext.put(PACKAGE, pkgElem.getQualifiedName() + ";");
        }

        // Implementation name
        String name = element.getSimpleName().toString();
        velocityContext.put(IMPL_NAME, name + NAME);

        // Target name
        velocityContext.put("targetName", name);

        // Superclass name
        TypeElement superClass = (TypeElement)typeUtils.asElement(element.getSuperclass());
        if(superClass != null) {
            String superClassName = superClass.getQualifiedName().toString();
            if(Emulation.isEmulated(superClass)) {
                superClassName = stripEmulationPrefix(superClassName);
            }

            if(!ReflectionAnnotationProcessor.getIgnoredClasses().contains(superClassName)) {
                velocityContext.put("superClass", superClassName + ".class");

                switch (superClassName) {
                    case "java.lang.Object":
                        velocityContext.put("superClassImpl", "nz.co.doltech.databind.reflect.base.ObjectClassReflection");
                        break;
                    case "com.google.gwt.core.client.JavaScriptObject":
                        velocityContext.put("superClassImpl", "nz.co.doltech.databind.reflect.gwt.base.JavaScriptObjectClassReflection");
                        break;
                    default:
                        velocityContext.put("superClassImpl", superClassName + NAME);
                }
            } else {
                velocityContext.put("superClass", "null");
            }
        }

        // Is abstract
        velocityContext.put("abstract", ElementUtils.hasModifier(element, Modifier.ABSTRACT));

        // Default constructor
        velocityContext.put("defaultCtor", ElementUtils.hasDefaultCtor(element));
    }

    private static String stripEmulationPrefix(String name) {
        return name.replace(Emulation.EMUL_PREFIX, "");
    }
}
