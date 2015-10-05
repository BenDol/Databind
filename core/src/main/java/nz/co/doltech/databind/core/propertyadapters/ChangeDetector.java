package nz.co.doltech.databind.core.propertyadapters;

import nz.co.doltech.databind.core.properties.PropertyChangedEvent;
import nz.co.doltech.databind.core.properties.PropertyChangedHandler;
import nz.co.doltech.databind.core.properties.Properties;

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