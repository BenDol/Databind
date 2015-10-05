package nz.co.doltech.databinder.classinfo.gwt.rebind;

import com.google.gwt.core.ext.*;

public class ClazzIncrementalGenerator extends IncrementalGenerator {

    private static final int VERSION = 140;

    private ClazzGenerator generator;

    @Override
    public RebindResult generateIncrementally(TreeLogger logger, GeneratorContext context, String typeName)
            throws UnableToCompleteException {
        if(generator == null) {
            generator = new ClazzGenerator();
        }

        String generatedClassName = generator.generate(logger, context, typeName);
        return new RebindResult(RebindMode.USE_ALL_NEW, generatedClassName);
    }

    @Override
    public long getVersionId() {
        return VERSION;
    }
}
