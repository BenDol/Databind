package nz.co.doltech.databind.apt.velocity;

import com.google.inject.Provides;
import nz.co.doltech.databind.apt.AbstractProcessorModule;
import org.apache.velocity.app.VelocityEngine;

import javax.inject.Singleton;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public abstract class AbstractVelocityModule extends AbstractProcessorModule {

    @Override
    protected void configure() {
        super.configure();

        bindConstant().annotatedWith(VelocityProperties.class).to(getVelocityProperties());
    }

    protected String getVelocityProperties() {
        return "velocity.properties";
    }

    @Provides
    @Singleton
    public VelocityEngine getVelocityEngine(@VelocityProperties String velocityProperties, Logger logger)
            throws IllegalStateException {
        try {
            InputStream inputStream = null;
            try {
                inputStream = this.getClass().getClassLoader().getResourceAsStream(velocityProperties);
                Properties properties = new Properties();
                properties.load(inputStream);
                return new VelocityEngine(properties);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            logger.severe("Cannot load velocity properties from " + velocityProperties);
            return null;
        }
    }
}