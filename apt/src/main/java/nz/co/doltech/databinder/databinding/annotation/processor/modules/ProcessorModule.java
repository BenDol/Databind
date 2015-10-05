package nz.co.doltech.databinder.databinding.annotation.processor.modules;

import nz.co.doltech.databinder.databinding.annotation.processor.BaseAnnotationProcessor.ProcInfo;

import java.util.List;

public interface ProcessorModule {

    List<String> getImports(ProcInfo procInfo);

    String getClassEntry(ProcInfo procInfo);
}
