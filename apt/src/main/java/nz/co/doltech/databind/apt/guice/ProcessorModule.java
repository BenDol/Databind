package nz.co.doltech.databind.apt.guice;

import nz.co.doltech.databind.apt.velocity.AbstractVelocityModule;

public class ProcessorModule extends AbstractVelocityModule {

    @Override
    protected void configure() {
        super.configure();
    }

    @Override
    protected String getVelocityProperties() {
        return "velocity.properties";
    }
}
