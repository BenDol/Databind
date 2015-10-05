package nz.co.doltech.databind.annotation.processor.modules;

import nz.co.doltech.databind.annotation.processor.BaseAnnotationProcessor;

import java.util.List;

public interface ProcessorModule {

    List<String> getImports(BaseAnnotationProcessor.ProcInfo procInfo);

    String getClassEntry(BaseAnnotationProcessor.ProcInfo procInfo);
}
