package nz.co.doltech.databind.classinfo.internal;

import java.util.ArrayList;
import java.util.List;

import nz.co.doltech.databind.classinfo.Method;

public abstract class MethodBase implements Method {
    private final Class<?> _returnValueClass;
    private final String _fieldName;
    private final Class<?>[] _parameterTypes;
    private List<Class<?>> _parameterTypesList;

    protected MethodBase(Class<?> clazz, String fieldName, Class<?>[] parameterTypes) {
        _returnValueClass = clazz;
        _fieldName = fieldName;
        _parameterTypes = parameterTypes;
    }

    @Override
    public String getName() {
        return _fieldName;
    }

    @Override
    public Class<?> getReturnType() {
        return _returnValueClass;
    }

    @Override
    public List<Class<?>> getParameterTypes() {
        if (_parameterTypesList == null) {
            _parameterTypesList = new ArrayList<>();
            for (Class<?> type : _parameterTypes) {
                _parameterTypesList.add(type);
            }
        }

        return _parameterTypesList;
    }
}
