package nz.co.doltech.databinder.databinding.propertyadapters;

import nz.co.doltech.databinder.databinding.properties.Properties;
import nz.co.doltech.databinder.databinding.properties.PropertyChangedEvent;
import nz.co.doltech.databinder.databinding.properties.PropertyChangedHandler;

/**
 * A PropertyAdapter that calls its onChange method when a property value changes
 * in the currently bound object.
 *
 * @author Arnaud
 */
public abstract class ChangeDetector extends WriteOnlyPropertyAdapter {
    PropertyChangedHandler handler = new PropertyChangedHandler() {
        @Override
        public void onPropertyChanged(PropertyChangedEvent event) {
            onChange(event.getSender(), event.getPropertyName());
        }
    };
    private Object reg;

    abstract protected void onChange(Object object, String property);

    @Override
    public void setValue(Object object) {
        if (reg != null) {
            Properties.removeHandler(reg);
            reg = null;
        }

        if (object != null) {
            reg = Properties.register(object, "*", handler);
        }
    }
}