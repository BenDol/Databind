package nz.co.doltech.databinder.databinding.gwt.annotation.processor;

import nz.co.doltech.databinder.databinding.annotation.processor.ObservableAnnotationProcessor;
import nz.co.doltech.databinder.databinding.annotation.processor.Template;
import nz.co.doltech.databinder.databinding.gwt.annotation.Observable;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.lang.annotation.Annotation;

@SupportedAnnotationTypes({
    "nz.co.doltech.databinder.databinding.gwt.annotation.Observable"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class GwtObservableAnnotationProcessor extends ObservableAnnotationProcessor {
    private final static String TEMPLATE_CLASS = "nz/co/doltech/databinder/databinding/gwt/annotation/processor/TemplateClass.txt";

    @Override
    protected String generateExtraImports(ProcInfo procInfo) {
        String extraImports = super.generateExtraImports(procInfo);
        if (!extraImports.contains("nz.co.doltech.databinder.classinfo.gwt.ClazzBundle")) {
            extraImports += "import nz.co.doltech.databinder.classinfo.gwt.ClazzBundle;\n";
        }
        if (!extraImports.contains("nz.co.doltech.databinder.classinfo.gwt.ReflectedClasses")) {
            extraImports += "import nz.co.doltech.databinder.classinfo.gwt.ReflectedClasses;\n";
        }
        return extraImports;
    }

    @Override
    protected StringBuilder generateClassEntry(ProcInfo procInfo) {
        return super.generateClassEntry(procInfo)
            .append(Template.fromResource(TEMPLATE_CLASS, BEGIN_INDEX).toString());
    }

    @Override
    protected int getInheritDepth(Annotation annotation) {
        if (annotation instanceof Observable) {
            Observable observable = ((Observable) annotation);
            int depth = observable.inheritDepth();
            return observable.inherit() ? depth : (depth != Observable.INHERIT_MAX ? depth : 0);
        }
        return -1;
    }

    @Override
    protected boolean canUseCopyConstructor(Annotation annotation) {
        return annotation instanceof Observable && ((Observable) annotation).copyConstructor();
    }
}
