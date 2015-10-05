package nz.co.doltech.databinder.classinfo.gwt.rebind;

import com.google.gwt.core.ext.*;

public class ClazzBundleIncrementalGenerator extends IncrementalGenerator {

    private static final int VERSION = 140;

    private ClazzBundleGenerator generator;

    @Override
    public RebindResult generateIncrementally(TreeLogger logger, GeneratorContext context, String typeName)
            throws UnableToCompleteException {
        if(generator == null) {
            generator = new ClazzBundleGenerator();
        }

        String generatedClassName = generator.generate(logger, context, typeName);
        return new RebindResult(RebindMode.USE_ALL_NEW, generatedClassName);
    }

    @Override
    public long getVersionId() {
        return VERSION;
    }
}
