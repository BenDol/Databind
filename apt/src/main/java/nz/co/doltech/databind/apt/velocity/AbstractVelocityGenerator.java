package nz.co.doltech.databind.apt.velocity;

import nz.co.doltech.databind.apt.ProcessorInfo;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import javax.inject.Provider;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractVelocityGenerator<T> {
    protected static final String IMPL_NAME = "implName";
    protected static final String PACKAGE = "package";

    private final Logger logger;
    private final VelocityEngine velocityEngine;
    private final Provider<VelocityContext> velocityContextProvider;

    public AbstractVelocityGenerator(Logger logger,
                                     VelocityEngine velocityEngine,
                                     Provider<VelocityContext> velocityContextProvider) {
        this.logger = logger;
        this.velocityEngine = velocityEngine;
        this.velocityContextProvider = velocityContextProvider;
    }

    public void mergeTemplate(Writer writer, T data) throws Exception {
        VelocityContext velocityContext = velocityContextProvider.get();

        populateVelocityContext(velocityContext, data);

        velocityEngine.mergeTemplate(getTemplate(), "UTF-8", velocityContext, writer);
    }

    protected abstract String getTemplate();

    protected abstract void populateVelocityContext(
        VelocityContext velocityContext, T data) throws VelocityException;
}
