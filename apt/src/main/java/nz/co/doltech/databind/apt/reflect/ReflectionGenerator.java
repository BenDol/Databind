package nz.co.doltech.databind.apt.reflect;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import nz.co.doltech.databind.apt.velocity.AbstractVelocityGenerator;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import javax.inject.Provider;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
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
            String superClassName = superClass.getQualifiedName().toString();
            velocityContext.put("superClass", superClassName);

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
        }

        // Is abstract
        velocityContext.put("abstract", hasModifier(element, Modifier.ABSTRACT));

        // Default constructor
        velocityContext.put("defaultCtor", hasDefaultCtor(element));
    }

    private boolean hasModifier(TypeElement element, Modifier modifier) {
        for(Modifier mod : element.getModifiers()) {
            if(mod.equals(modifier)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasDefaultCtor(TypeElement element) {
        for(ExecutableElement ctor : ElementFilter.constructorsIn(element.getEnclosedElements())) {
            if (ctor.getParameters().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
