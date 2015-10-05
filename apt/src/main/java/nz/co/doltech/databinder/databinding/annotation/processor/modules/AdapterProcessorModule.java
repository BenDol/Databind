package nz.co.doltech.databinder.databinding.annotation.processor.modules;

import nz.co.doltech.databinder.databinding.annotation.processor.BaseAnnotationProcessor;

import java.util.ArrayList;
import java.util.List;

public class AdapterProcessorModule implements ProcessorModule {
    @Override
    public List<String> getImports(BaseAnnotationProcessor.ProcInfo procInfo) {
        return new ArrayList<>();
    }

    @Override
    public String getClassEntry(BaseAnnotationProcessor.ProcInfo procInfo) {
        return "";
    }
}
