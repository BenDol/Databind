package nz.co.doltech.databinder.classinfo.internal;

import java.util.ArrayList;
import java.util.List;

import nz.co.doltech.databinder.classinfo.Field;
import nz.co.doltech.databinder.classinfo.Method;

public class ObjectClazz extends nz.co.doltech.databinder.classinfo.internal.ClazzBase<java.lang.Object> {
    public ObjectClazz() {
        super(java.lang.Object.class, "Object", null);
    }

    @Override
    protected List<Field> _getDeclaredFields() {
        ArrayList<Field> res = new ArrayList<Field>();
        return res;
    }

    @Override
    protected List<Method> _getMethods() {
        return new ArrayList<Method>();
    }

    @Override
    public java.lang.Object NEW() {
        return new java.lang.Object();
    }

    @Override
    protected void _ensureSuperClassInfoRegistered() {
    }
}
