package nz.co.doltech.databinder.databinding.tools;

import nz.co.doltech.databinder.databinding.Binder;
import nz.co.doltech.databinder.databinding.DataBinding;
import nz.co.doltech.databinder.databinding.Mode;
import nz.co.doltech.databinder.databinding.propertyadapters.PropertyAdapter;
import nz.co.doltech.databinder.databinding.propertyadapters.WriteOnlyPropertyAdapter;

/**
 * An instance of this class will allow to bind values from one and
 * one source only. Any new registration will automatically remove the
 * previous one.
 *
 * @author Arnaud
 */
public class SmartRegistration {
    DataBinding dataBinding;
    PropertyAdapter adapter;

    /**
     * Creates a SmartRegistration object that will throw values
     * at the specified adapter.
     * <p/>
     * <p>One simple use is to use a {@link WriteOnlyPropertyAdapter}
     * to receive values from the registered data binding
     *
     * @param adapter The property adapter
     */
    public SmartRegistration(PropertyAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Registers the databinding source. If any previous source
     * was binded, its bindings are freed.
     *
     * @param source The object which is the source of the binding
     * @param path   The path of the data
     */
    public void register(Object source, String path) {
        unregister();

        dataBinding = Binder.bind(source, path).mode(Mode.OneWay).to(adapter).activate();
    }

    /**
     * Frees the current data binding, if any
     */
    public void unregister() {
        if (dataBinding != null) {
            dataBinding.terminate();
            dataBinding = null;
        }
    }
}