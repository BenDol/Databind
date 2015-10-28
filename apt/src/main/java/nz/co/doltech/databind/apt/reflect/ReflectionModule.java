package nz.co.doltech.databind.apt.reflect;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import nz.co.doltech.databind.apt.velocity.AbstractVelocityModule;

public class ReflectionModule extends AbstractVelocityModule {

    @Override
    protected void configure() {
        super.configure();

        install(new FactoryModuleBuilder().build(ReflectionGenerator.Factory.class));
    }
}
