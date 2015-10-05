package nz.co.doltech.databind.core.properties;

/**
 * Interface through which one receives {@link PropertyChangedEvent}
 */
public interface PropertyChangedHandler {
    void onPropertyChanged(PropertyChangedEvent event);
}