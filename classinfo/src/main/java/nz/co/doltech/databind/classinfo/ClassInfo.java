package nz.co.doltech.databind.classinfo;

import java.util.Set;

/**
 * Frontal singleton entry point providing the Reflection system API
 *
 * @author Arnaud Tournier
 *         (c) LTE Consulting - 2015
 *         http://www.doltech.co.nz
 */
public class ClassInfo {
    private static IClassInfo impl = ClassInfoProvider.get();

    /**
     * Obtain a runtime type information on a class.<br><br>
     * <p/>
     * Throws a RuntimeException if the type information provider is not found.
     *
     * @param clazz The class object for which type information is required
     * @return The runtime information interface
     */
    public static <T> Clazz<T> clazz(Class<T> clazz) {
        return impl.clazz(clazz);
    }

    /**
     * Register a runtime type information provider
     *
     * @param clazz
     */
    public static <T> void registerClazz(Clazz<T> clazz) {
        impl.registerClazz(clazz);
    }

    /**
     * Obtain a runtime type information on a class.
     *
     * @param name Name of the class for which type information is required
     * @return The runtime information interface
     */
    public static Clazz<?> findClazz(String name) {
        return impl.findClazz(name);
    }

    /**
     * Obtain a runtime type information on a class.
     *
     * @param clazz The class object for which type information is required
     * @return The runtime information interface
     */
    public static <T> Clazz<T> findClazz(Class<T> clazz) {
        return impl.findClazz(clazz);
    }

    /**
     * Retrieve the set of registered type information providers
     *
     * @return
     */
    public static Set<Class<?>> getRegisteredClazz() {
        return impl.getRegisteredClazz();
    }
}
