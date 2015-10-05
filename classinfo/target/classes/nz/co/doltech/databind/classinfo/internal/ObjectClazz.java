package nz.co.doltech.databind.classinfo.internal;

import java.util.ArrayList;
import java.util.List;

import nz.co.doltech.databind.classinfo.Field;
import nz.co.doltech.databind.classinfo.Method;

public class ObjectClazz extends ClazzBase<Object> {

    public ObjectClazz() {
        super(Object.class, "Object", null);
    }

    @Override
    protected List<Field> _getDeclaredFields() {
        return new ArrayList<>();
    }

    @Override
    protected List<Method> _getMethods() {
        return new ArrayList<>();
    }

    @Override
    public Object newInstance() {
        return new Object();
    }

    @Override
    protected void _ensureSuperClassInfoRegistered() {
    }
}
