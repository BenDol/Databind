package nz.co.doltech.databind.apt.reflect;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import nz.co.doltech.databind.apt.velocity.AbstractVelocityGenerator;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import javax.inject.Provider;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.logging.Logger;

public class ReflectionGenerator extends AbstractVelocityGenerator<TypeMirror> {

    private final static Logger logger = Logger.getLogger(ReflectionGenerator.class.getName());

    public static final String NAME = "_ReflectImpl";

    public interface Factory {
        ReflectionGenerator createReflection(
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
        Name pkgName = elementUtils.getPackageOf(element).getQualifiedName();
        velocityContext.put(PACKAGE, pkgName + ";");

        // Implementation name
        String name = element.getSimpleName().toString();
        velocityContext.put(IMPL_NAME, name + NAME);

        // Target name
        velocityContext.put("targetName", name);

        // Superclass name
        TypeElement superClass = (TypeElement)typeUtils.asElement(element.getSuperclass());
        if(superClass != null) {
            velocityContext.put("superClass", superClass.getQualifiedName());
        } else {
            velocityContext.put("superClass", "java.lang.Object");
        }
    }
}