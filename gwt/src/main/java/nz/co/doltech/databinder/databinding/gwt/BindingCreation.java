package nz.co.doltech.databinder.databinding.gwt;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

import nz.co.doltech.databinder.databinding.Converter;
import nz.co.doltech.databinder.databinding.Mode;
import nz.co.doltech.databinder.databinding.propertyadapters.PropertyAdapter;
import nz.co.doltech.databinder.databinding.gwt.propertyadapters.ListBoxPropertyAdapter;
import nz.co.doltech.databinder.databinding.gwt.propertyadapters.ValuePropertyAdapter;

/**
 * Second part of the fluent API for Data Binding. When a binding is
 * prepared by calling a method on the Binder class, an instance of
 * this class is returned to finish the binding creation process.
 *
 * @author Arnaud Tournier
 *         (c) LTE Consulting - 2015
 *         http://www.doltech.co.nz
 */
public class BindingCreation extends nz.co.doltech.databinder.databinding.BindingCreation {
    protected boolean deferActivate;

    public BindingCreation(PropertyAdapter source) {
        super(source);
    }

    /**
     * Final step, defines the data binding destination and activates the
     * binding.
     * <p/>
     * The object used as the binding's destination is a HasValue widget, like a
     * TextBox. The binding system will the use setValue, getValue and
     * addValueChangeHandler methods to set, get and get change notifications on
     * the @param widget parameter.
     *
     * @param hasValue The {@link HasValue} object
     * @return The DataBinding object
     */
    public DataBinding to(HasValue<?> hasValue) {
        return to(new ValuePropertyAdapter(hasValue));
    }

    /**
     * Final step, defines the data binding destination and activates the
     * binding.
     * <p/>
     * The object used as the binding's destination is a ListBox widget.
     * The binding system will the use setValue, getValue and addChangeHandler
     * methods to set, get and get change notifications on the @param widget parameter.
     *
     * @param listBox The widget
     * @return The DataBinding object
     */
    public DataBinding to(ListBox listBox) {
        return to(new ListBoxPropertyAdapter(listBox));
    }

    @Override
    public BindingCreation log(String prefix) {
        super.log(prefix);
        return this;
    }

    /**
     * Second step, parameters.
     * <p/>
     * The created data binding will be activated at the next event loop. The
     * Scheduler.get().scheduleDeferred() method will be used.
     *
     * @return The Binder to continue specifying the data binding
     */
    public BindingCreation deferActivate() {
        deferActivate = true;

        return this;
    }

    @Override
    public BindingCreation mode(Mode mode) {
        super.mode(mode);
        return this;
    }

    @Override
    public BindingCreation withConverter(Converter converter) {
        super.withConverter(converter);
        return this;
    }

    @Override
    public DataBinding to(PropertyAdapter destination) {
        // create the binding according to the parameters
        DataBinding binding = new DataBinding(source, destination, mode, converter, logPrefix);

        // activate the binding : launch a value event
        if (deferActivate)
            binding.deferActivate();
        else
            binding.activate();

        return binding;
    }
}
