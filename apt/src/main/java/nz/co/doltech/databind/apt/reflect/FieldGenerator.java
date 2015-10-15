package nz.co.doltech.databind.apt.reflect;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import nz.co.doltech.databind.apt.velocity.AbstractVelocityGenerator;
import nz.co.doltech.databind.util.ModifierBuilder;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import javax.inject.Provider;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.logging.Logger;

public class FieldGenerator extends AbstractVelocityGenerator<FieldGenerator.FieldPair> {

    private final static Logger logger = Logger.getLogger(FieldGenerator.class.getName());

    public static final String NAME = "_FieldReflection";

    public static class FieldPair {
        VariableElement field;
        TypeElement parent;

        public FieldPair(VariableElement field, TypeElement parent) {
            this.field = field;
            this.parent = parent;
        }
    }

    private final Types typeUtils;
    private final String velocityTemplate;

    @Inject
    public FieldGenerator(Provider<VelocityContext> velocityContextProvider,
                          VelocityEngine velocityEngine,
                          Types typeUtils,
                          @Assisted("velocityTemplate") String velocityTemplate) {
        super(logger, velocityEngine, velocityContextProvider);

        this.typeUtils = typeUtils;
        this.velocityTemplate = velocityTemplate;
    }

    @Override
    protected String getTemplate() {
        return velocityTemplate;
    }

    @Override
    protected void populateVelocityContext(VelocityContext velocityContext, FieldPair pair) throws VelocityException {
        VariableElement field = pair.field;
        TypeElement parent = pair.parent;

        // Implementation name
        String name = field.getSimpleName().toString();
        velocityContext.put(IMPL_NAME, name + NAME);

        // Field name
        velocityContext.put("fieldName", name);

        // Target name
        String targetName = field.asType().toString();
        if(targetName.contains("<")) {
            targetName = targetName.substring(0, targetName.indexOf("<"));
        }
        velocityContext.put("targetName", targetName);

        // Modifier
        velocityContext.put("modifier", getFieldModifier(field));

        // Class cast
        TypeMirror target = field.asType();
        if(target.getKind().isPrimitive()) {
            TypeElement boxed = typeUtils.boxedClass(typeUtils.getPrimitiveType(target.getKind()));
            velocityContext.put("castClass", boxed.getQualifiedName());
        } else {
            velocityContext.put("castClass", target);
        }

        // Parent class name
        Name parentClass = parent.getQualifiedName();
        velocityContext.put("parentClass", parentClass);
    }

    private String getFieldModifier(VariableElement field) {
        ModifierBuilder mb = new ModifierBuilder();
        for(Modifier mod : field.getModifiers()) {
            mb.append(String.valueOf(mod.ordinal()));
        }

        return mb.toString();
    }
}
