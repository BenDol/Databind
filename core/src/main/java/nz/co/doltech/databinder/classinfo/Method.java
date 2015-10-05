package nz.co.doltech.databinder.classinfo;

import java.util.List;

/**
 * Represents a Java method
 *
 * @author Arnaud
 */
public interface Method {
    String getName();

    Class<?> getReturnType();

    List<Class<?>> getParameterTypes();

    Object invoke(Object target, Object... parameters);
}
