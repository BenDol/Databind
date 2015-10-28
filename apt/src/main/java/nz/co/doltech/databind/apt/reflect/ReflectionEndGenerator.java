package nz.co.doltech.databind.apt.reflect;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import nz.co.doltech.databind.apt.velocity.AbstractVelocityGenerator;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import javax.inject.Provider;
import java.util.List;
import java.util.logging.Logger;

public class ReflectionEndGenerator extends AbstractVelocityGenerator<List<String>> {

    private final static Logger logger = Logger.getLogger(ReflectionEndGenerator.class.getName());

    private final String velocityTemplate;

    @Inject
    public ReflectionEndGenerator(Provider<VelocityContext> velocityContextProvider,
                                  VelocityEngine velocityEngine,
                                  @Assisted("velocityTemplate") String velocityTemplate) {
        super(logger, velocityEngine, velocityContextProvider);

        this.velocityTemplate = velocityTemplate;
    }

    @Override
    protected String getTemplate() {
        return velocityTemplate;
    }

    @Override
    protected void populateVelocityContext(VelocityContext velocityContext, List<String> fields) throws VelocityException {
        velocityContext.put("fields", fields);
    }
}
