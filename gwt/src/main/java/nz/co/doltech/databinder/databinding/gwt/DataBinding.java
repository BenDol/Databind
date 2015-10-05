package nz.co.doltech.databinder.databinding.gwt;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import nz.co.doltech.databinder.databinding.Converter;
import nz.co.doltech.databinder.databinding.Mode;
import nz.co.doltech.databinder.databinding.propertyadapters.PropertyAdapter;

/**
 * DataBinding for GWT implementation.
 *
 * @author Ben Dol
 */
public class DataBinding extends nz.co.doltech.databinder.databinding.DataBinding {

    public DataBinding(Object source, String sourceProperty, Object destination,
                       String destinationProperty, Mode bindingMode) {
        super(source, sourceProperty, destination, destinationProperty, bindingMode);
    }

    public DataBinding(PropertyAdapter source, PropertyAdapter destination, Mode bindingMode,
                       Converter converter, String logPrefix) {
        super(source, destination, bindingMode, converter, logPrefix);
    }

    @Override
    public DataBinding activate() {
        return (DataBinding) super.activate();
    }

    @Override
    public DataBinding suspend() {
        return (DataBinding) super.suspend();
    }

    /**
     * Activates the data binding using the deferred scheduler.
     *
     * @see #activate()
     */
    public void deferActivate() {
        log("deferred activation...");

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                activate();
            }
        });
    }
}
